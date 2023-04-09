package com.kob.backend.service.user.account;

import java.util.Map;

// 定义接口
public interface LoginService {
    public Map<String, String> getToken(String username, String password);
}
