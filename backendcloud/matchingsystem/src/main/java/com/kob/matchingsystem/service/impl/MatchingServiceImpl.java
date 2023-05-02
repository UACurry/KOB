package com.kob.matchingsystem.service.impl;

import com.kob.matchingsystem.service.MatchingService;
import com.kob.matchingsystem.service.impl.utils.MatchingPool;
import org.springframework.stereotype.Service;

@Service
public class MatchingServiceImpl implements MatchingService {

//    定义一个线程 因为全局只有一个 matchingpool 所以可以静态
    public final static MatchingPool matchingPool = new MatchingPool();

    @Override
    public String addPlayer(Integer userId, Integer rating, Integer botId) {
        System.out.println("add_player" + userId + " " + rating);
        matchingPool.addPlayer(userId,rating,botId);
        return "add success";
    }

    @Override
    public String removePlayer(Integer userId) {
        System.out.println("remove" + " " + userId);
        matchingPool.removePlayer(userId);
        return "remove success";
    }
}
