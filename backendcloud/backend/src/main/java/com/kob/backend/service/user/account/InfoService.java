package com.kob.backend.service.user.account;

import java.util.Map;
// 定义接口

//根据用户token获得用户信息
public interface InfoService {

    public Map<String, String> getinfo();
}
