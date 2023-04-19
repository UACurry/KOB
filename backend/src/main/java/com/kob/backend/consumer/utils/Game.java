package com.kob.backend.consumer.utils;

import java.util.Random;

// 实现一个类 用来管理整个游戏流程
public class Game {
//    一些私有变量
    final private Integer rows;
    final private Integer cols;
//    障碍物数量
    final private Integer inner_walls_count;

//    存一个地图
    private int[][] g;

    final private static int[] dx = {-1, 0, 1, 0}, dy = {0, 1, 0, -1};

//    初始化一个构造函数
    public Game(Integer rows, Integer cols, Integer inner_walls_count) {
        this.rows = rows;
        this.cols = cols;
        this.inner_walls_count = inner_walls_count;
        this.g = new int[rows][cols];
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
}
