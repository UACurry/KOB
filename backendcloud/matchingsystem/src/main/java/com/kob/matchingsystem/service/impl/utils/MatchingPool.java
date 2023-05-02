package com.kob.matchingsystem.service.impl.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;

// 维护我们的线程
// 多线程
@Component
public class MatchingPool extends Thread{
//    存所有用户 players 是所有匹配线程共用的 也有传入参数的线程 需要加锁
    private static List<Player> players = new ArrayList<>();

    private final ReentrantLock lock = new ReentrantLock();

    private static RestTemplate restTemplate;

//    请求backend端
    private final static String startGameUrl = "http://127.0.0.1:1799/pk/start/game/";

    @Autowired
    public void setRestTemplate(RestTemplate restTemplate){
        MatchingPool.restTemplate = restTemplate;
    }


    public void addPlayer(Integer userId, Integer rating, Integer botId) {
        lock.lock();
        try {
            players.add(new Player(userId, rating, 0, botId));
        } finally {
            lock.unlock();
        }
    }

    public void removePlayer(Integer userId) {
        lock.lock();
        try {
//            将没有删的都存下来
            List<Player> newPlayers = new ArrayList<>();
            for (Player player: players) {
//                判断是否和要删的相等
                if (!player.getUserId().equals(userId)) {
                    newPlayers.add(player);
                }
            }
//            赋值
            players = newPlayers;
        } finally {
            lock.unlock();
        }
    }

    private void increaseWaitingTime() {  // 将所有当前玩家的等待时间加1
        for (Player player: players) {
            player.setWaitingTime(player.getWaitingTime() + 1);
        }
    }

    private boolean checkMatched(Player a, Player b) {  // 判断两名玩家是否匹配
//        计算分差
        int ratingDelta = Math.abs(a.getRating() - b.getRating());
//        计算等待时间 能否被等待时间的最小值 * 10  满足分差
        int waitingTime = Math.min(a.getWaitingTime(), b.getWaitingTime());
//      你情我愿 A 和 B 都要接受
        return ratingDelta <= waitingTime * 10;
    }

//    将结果返回给 backend 端 需要用一个sendresult
    private void sendResult(Player a, Player b) {  // 返回匹配结果
        System.out.println("send result: " + a + " " + b);
        MultiValueMap<String, String> data = new LinkedMultiValueMap<>();
        data.add("a_id", a.getUserId().toString());
        data.add("a_bot_id",a.getBotId().toString());
        data.add("b_id", b.getUserId().toString());
        data.add("b_bot_id",b.getBotId().toString());
        //    请求backend端 将信息发给backend端 也就是 把匹配好的信息 给backend
//        backend 从而进行接受 接受在 startGameController
        restTemplate.postForObject(startGameUrl, data, String.class);
    }


    private void matchPlayers() {  // 尝试匹配所有玩家
        System.out.println("match players: " + players.toString());
//        判断哪些已经被匹配过了
        boolean[] used = new boolean[players.size()];
//        越往后的玩家 等待时间越长 因为 添加玩家时候 用的add函数
        for (int i = 0; i < players.size(); i ++ ) {
            if (used[i]) continue;
            for (int j = i + 1; j < players.size(); j ++ ) {
                if (used[j]) continue;
//                取出两名玩家
                Player a = players.get(i), b = players.get(j);
//                判断是否能够匹配
                if (checkMatched(a, b)) {
                    used[i] = used[j] = true;
//                    返回结果
                    sendResult(a, b);
                    break;
                }
            }
        }

//        将已经用过的玩家删掉 从匹配池中
        List<Player> newPlayers = new ArrayList<>();
        for (int i = 0; i < players.size(); i ++ ) {
            if (!used[i]) {
                newPlayers.add(players.get(i));
            }
        }
        players = newPlayers;
    }


    //    定义多线程需要重写的函数
    @Override
    public void run() {
//        死循环 每一秒匹配一下
        while (true) {
            try {
                Thread.sleep(1000);
                lock.lock();
                try {
//                    下面两个函数 都设计到 players 所以会有读写冲突 所以需要加锁
//                    每一秒 等待时间加一 所有玩家
                    increaseWaitingTime();
                    matchPlayers();
                } finally {
                    lock.unlock();
                }

            } catch (InterruptedException e) {
                e.printStackTrace();
                break;
            }
        }
    }
}
