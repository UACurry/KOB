package com.kob.backend.service.record;
import com.alibaba.fastjson.JSONObject;

public interface GetRecordListService {
//    传入一个页面的编号
    JSONObject getList(Integer page);
}
