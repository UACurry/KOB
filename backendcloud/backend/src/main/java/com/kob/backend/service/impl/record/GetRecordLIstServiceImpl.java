package com.kob.backend.service.impl.record;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kob.backend.mapper.RecordMapper;
import com.kob.backend.mapper.UserMapper;
import com.kob.backend.pojo.Record;
import com.kob.backend.pojo.User;
import com.kob.backend.service.record.GetRecordListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;

@Service
public class GetRecordLIstServiceImpl implements GetRecordListService {
    @Autowired
    private RecordMapper recordMapper;

//    因为头像和用户名是现查的
    @Autowired
    private UserMapper userMapper;

    @Override
    public JSONObject getList(Integer page) {
//        每一页传 10 个
        IPage<Record> recordIPage = new Page<>(page, 10);
        QueryWrapper<Record> queryWrapper = new QueryWrapper<>();
//        先将所有record 按 id 降序排序
        queryWrapper.orderByDesc("id");
//        返回第 page 页内容
        List<Record> records = recordMapper.selectPage(recordIPage, queryWrapper).getRecords();

//        将信息返回前端
        JSONObject resp = new JSONObject();
//        存储每一局信息
        List<JSONObject> items = new LinkedList<>();
        for (Record record: records) {
            User userA = userMapper.selectById(record.getAId());
            User userB = userMapper.selectById(record.getBId());

            JSONObject item = new JSONObject();
            item.put("a_photo", userA.getPhoto());
            item.put("a_username", userA.getUsername());
            item.put("b_photo", userB.getPhoto());
            item.put("b_username", userB.getUsername());

//            后端查询对战结果
            String result = "平局";
            if ("A".equals(record.getLoser())) result = "B胜";
            else if ("B".equals(record.getLoser())) result = "A胜";
            item.put("result", result);
//            当前对战信息
            item.put("record", record);
            items.add(item);
        }
        resp.put("records", items);
//        需要知道一共有多少页 告诉前端 一共有多少页  null 无条件返回
        resp.put("records_count", recordMapper.selectCount(null));

        return resp;
    }
}
