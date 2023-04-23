package com.kob.backend.consumer.utils;

import com.alibaba.fastjson.JSONObject;
import com.kob.backend.consumer.WebSocketServer;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.locks.ReentrantLock;

// 实现一个类 用来管理整个游戏流程  主要含有地图的创建
//extends Thread 让他变为多线程
public class Game extends Thread{
//    一些私有变量
    private final Integer rows;
    private final Integer cols;
//    障碍物数量
    private final Integer inner_walls_count;

//    存一个地图
    private int[][] g;

    private final static int[] dx = {-1, 0, 1, 0}, dy = {0, 1, 0, -1};

//    存储一下每名玩家
    private final Player playerA;
    private final Player playerB;

//    下一步操作
    private Integer nextStepA = null;
    private Integer nextStepB = null;

//    锁
    private ReentrantLock lock = new ReentrantLock();

    private String status = "playing"; // playing ->> finished

//    记录一下谁输了
    private String loser = "";

//    初始化一个构造函数  初始时候 需要传入两名玩家 id
    public Game(Integer rows, Integer cols, Integer inner_walls_count, Integer idA, Integer idB) {
        this.rows = rows;
        this.cols = cols;
        this.inner_walls_count = inner_walls_count;
        this.g = new int[rows][cols];
        playerA = new Player(idA, rows - 2, 1, new ArrayList<>());
        playerB = new Player(idB, 1, cols - 2, new ArrayList<>());
    }

//    为了让WebSocket中能够获取到 Game中 的player
    public Player getPlayerA(){
        return playerA;
    }
    public Player getPlayerB(){
        return playerB;
    }

//    在WebSocket 里面调用
//    这里需要加锁  防止读写冲突 因为 game中的 nextstep 负责读取 而setNextStepA 负责修改 所以需要加锁
//    也就是两个线程 （nextstep 和 而setNextStepA） 同时读写一个变量 所以要加锁
    public void setNextStepA(Integer nextStepA) {
        lock.lock();
        try {
            this.nextStepA = nextStepA;
        } finally {
            lock.unlock();
        }
    }

    public void setNextStepB(Integer nextStepB) {
        lock.lock();
        try {
            this.nextStepB = nextStepB;
        } finally {
            lock.unlock();
        }
    }


    //    返回地图
    public int[][] getG(){
        return g;
    }

//    检测连通性
    private boolean check_connectivity(int sx, int sy, int tx, int ty) {
        if (sx == tx && sy == ty) return true;
        g[sx][sy] = 1;

        for (int i = 0; i < 4; i ++ ) {
            int x = sx + dx[i], y = sy + dy[i];
            if (x >= 0 && x < this.rows && y >= 0 && y < this.cols && g[x][y] == 0) {
                if (check_connectivity(x, y, tx, ty)) {
                    g[sx][sy] = 0;
                    return true;
                }
            }
        }

//        恢复现场
        g[sx][sy] = 0;
        return false;
    }

//    画地图
    private boolean draw(){
//        0 表示 空地
        for (int i = 0; i < this.rows; i ++ ) {
            for (int j = 0; j < this.cols; j ++ ) {
                g[i][j] = 0;
            }
        }

//        给四周加墙
        for (int r = 0; r < this.rows; r ++ ) {
            g[r][0] = g[r][this.cols - 1] = 1;
        }
        for (int c = 0; c < this.cols; c ++ ) {
            g[0][c] = g[this.rows - 1][c] = 1;
        }

        // 创建随机障碍物 中心对称放入
        Random random = new Random();
        for (int i = 0; i < this.inner_walls_count / 2; i ++ ) {
            for (int j = 0; j < 1000; j ++ ) {
                int r = random.nextInt(this.rows);
                int c = random.nextInt(this.cols);
//                判断随机的是否有重复 的墙
                if (g[r][c] == 1 || g[this.rows - 1 - r][this.cols - 1 - c] == 1)
                    continue;

                // 不能覆盖左下角右下角
                if (r == this.rows - 2 && c == 1 || r == 1 && c == this.cols - 2)
                    continue;

//                中心对称
                g[r][c] = g[this.rows - 1 - r][this.cols - 1 - c] = 1;
                break;
            }
        }
//        输入起点和终点的坐标
        return check_connectivity(this.rows - 2, 1, 1, this.cols - 2);
    }

    public void createMap(){
//        随机一千次 在创建地图时候 防止出现无法创建的地图
        for (int i = 0; i < 1000; i ++ ) {
            if (draw())
                break;
        }
    }

//    实现下一步操作 等待两名玩家下一步操作
//    也就是两个线程 （nextstep 和 而setNextStepA） 同时读写一个变量 所以要加锁
    public boolean nextStep(){
//        先睡200ms
        try{
            Thread.sleep(200);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

//        拿到锁之后进行加锁之后的操作
        for(int i = 0;i < 50;i++){
            try {
//                循环5秒钟 看是否有玩家的输入 每一秒都判断一下是否有玩家输入
                Thread.sleep(100);
//                所以要在 循环 里进行加锁 lock.lock() 和 解锁 lock.unlock() 的操作，
                //这样可能第一次没有读取到操作，但是第一次循环结束后会执行 lock.unlock() 的操作，把锁解开，
                //这样 Client1 和 Client2 输入操作的时候就可以通过 setNextStepA 和 setNextStepB 拿到锁，
                //拿到锁之后就可以进行赋值操作，然后再把锁解开。
                //之后 nextStep() 方法中 sleep 1 秒结束后，在进行拿锁，然后判断玩家是否有读入操作，再解锁的逻辑~
                lock.lock();
//                拿到锁之后再判断是否为空
                try {
                    if(nextStepA != null && nextStepB != null){
//                        把每一步加入到 steps中
                        playerA.getSteps().add(nextStepA);
                        playerB.getSteps().add(nextStepB);
//                        下一步操作获取到 了 返回true
                        return true;
                    }
                }finally {
                    lock.unlock();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return  false;
    }

//    判断是否合法
    private boolean check_valid(List<Cell> cellsA, List<Cell> cellsB) {
        int n = cellsA.size();
//        取最后一位 也就是蛇头位置
        Cell cell = cellsA.get(n - 1);
//        如果是墙
        if (g[cell.x][cell.y] == 1) return false;

//        否则 除了最后一位 以外 是否有重合 蛇的身体 是否有重合 意思也就是 新增长的 或者 之前的 不能撞到自己身体
        for (int i = 0; i < n - 1; i ++ ) {
            if (cellsA.get(i).x == cell.x && cellsA.get(i).y == cell.y)
                return false;
        }

//        同上
        for (int i = 0; i < n - 1; i ++ ) {
            if (cellsB.get(i).x == cell.x && cellsB.get(i).y == cell.y)
                return false;
        }

        return true;
    }

    //        判断下一步操作是否合法
    private void judge(){
//        把两条蛇取出来
        List<Cell> cellsA  = playerA.getCells();
        List<Cell> cellsB  = playerB.getCells();

//        判断合法性 因为上面  check_valid 的写法 所以拿到两个valid 判断 判断到底是哪个用户的非法操作
        boolean validA = check_valid(cellsA, cellsB);
        boolean validB = check_valid(cellsB, cellsA);
//        但凡有一个不合法
        if (!validA || !validB) {
            status = "finished";

            if (!validA && !validB) {
                loser = "all";
            } else if (!validA) {
                loser = "A";
            } else {
                loser = "B";
            }
        }

    }

    //    向所有人广播信息 一个辅助函数
    private void sendAllMessage(String message){
//        获取玩家A的websocket链接
        WebSocketServer.users.get(playerA.getId()).SendMessage(message);
        WebSocketServer.users.get(playerB.getId()).SendMessage(message);
    }

//    向两个客户端传递信息
    private void sendMove(){
        lock.lock();
        try {
            JSONObject resp = new JSONObject();
            resp.put("event","move");
//            向前端返回
            resp.put("a_direction",nextStepA);
            resp.put("b_direction",nextStepB);
            sendAllMessage(resp.toJSONString());
//            进行下一步之前 清空
            nextStepA = nextStepB = null;
        }finally {
            lock.unlock();
        }
    }


    private void sendResult(){ // 向两名玩家 发送消息
        JSONObject resp = new JSONObject();
        resp.put("event","result");
        resp.put("loser",loser);
        sendAllMessage(resp.toJSONString());
    }

//    重写一下 多线程的入口函数 每一个线程都会run一次
    @Override
    public void run() {
//        循环一千步 肯定不会走到1000步
        for(int i = 0;i< 1000;i++){
//            是否在五秒内获取到下一步
            if(nextStep()){
//                判断两名玩家
                judge();

//                如果合法
                if(status.equals("playing")){
//                    将这步操作广播给两名玩家 从服务器广播
                    sendMove();
                }
                else {
                    //向两名玩家发送消息 发送结果信息
                    sendResult();
                    break;
                }
            }else{
                status = "finished";
//                涉及到读入 所以加锁 因为前面可能在修改
                lock.lock();
                try {
                    if(nextStepA == null && nextStepB == null){
                        loser = "all";
                    } else if (nextStepA == null) {
                        loser = "A";
                    }else {
                        loser = "B";
                    }
                } finally {
                    lock.unlock();
                }

//                向两名玩家发送消息 发送结果信息
                sendResult();

//                游戏结束
                break;
            }
        }
    }
}