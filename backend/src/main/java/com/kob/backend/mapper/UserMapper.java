package com.kob.backend.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.kob.backend.pojo.User;
import org.apache.ibatis.annotations.Mapper;

//也叫Dao层）：将pojo层的class中的操作，映射成sql语句
@Mapper
// 使用mybatis plus 需要继承 接口
public interface UserMapper extends BaseMapper<User> {
}
