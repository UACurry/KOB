package com.kob.backend.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.kob.backend.pojo.Bot;
import org.apache.ibatis.annotations.Mapper;

@Mapper
// 使用mybatis plus 需要继承 接口
public interface BotMapper extends BaseMapper<Bot> {
}