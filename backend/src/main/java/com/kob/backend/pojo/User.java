package com.kob.backend.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

//用来映射 将数据库中的表对应成Java中的Class
@Data // 自动添加构造函数
@NoArgsConstructor
@AllArgsConstructor
public class User {
    private Integer id;
    private String username;
    private String password;

    public User(int userID, String username, String password) {
    }
}
