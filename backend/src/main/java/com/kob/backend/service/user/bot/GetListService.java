package com.kob.backend.service.user.bot;

import com.kob.backend.pojo.Bot;

import java.util.List;
import java.util.Map;

// 查询 bot  所有的bot
public interface GetListService {
//    getlist 可以不用传参数 因为自己的id存在token里面的
    public List<Bot> getlist();
}
