package com.kob.matchingsystem.service;

// 匹配池微服务
public interface MatchingService {
//    添加时候 关注两个 一个是userid 另一个是天梯分
    String addPlayer(Integer userId, Integer rating, Integer botId);
    String removePlayer(Integer userId);
}
