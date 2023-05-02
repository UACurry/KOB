package com.kob.backend.service.pk;

// 主服务器 用于接收 botrunningsystem 的bot的代码的返回
public interface ReceiveBotMoveService {
    String receiveBotMove(Integer userId, Integer direction);
}
