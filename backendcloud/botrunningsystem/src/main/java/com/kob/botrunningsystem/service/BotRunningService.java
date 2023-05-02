package com.kob.botrunningsystem.service;

public interface BotRunningService {
//    userId bot属于谁   botcode    input 输入整个地图 表示哪里有障碍物
    String addBot(Integer userId, String botCode, String input);
}
