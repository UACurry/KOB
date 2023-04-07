package com.kob.backend.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

//用来映射 将数据库中的表对应成Java中的Class
@Data // 自动添加构造函数
@NoArgsConstructor
@AllArgsConstructor
public class User {
//    如果需要自增必须增加的注解  33 分钟
    @TableId(type = IdType.AUTO)
    private Integer id;
    private String username;
    private String password;
    private String photo;
}
