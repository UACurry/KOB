package com.kob.backend.service.impl.user.bot;

import com.kob.backend.mapper.BotMapper;
import com.kob.backend.pojo.Bot;
import com.kob.backend.pojo.User;
import com.kob.backend.service.impl.util.UserDetailsImpl;
import com.kob.backend.service.user.bot.AddService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import javax.xml.crypto.Data;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class AddServiceImpl implements AddService {
    @Autowired
    private BotMapper botMapper;


    @Override
    public Map<String, String> add(Map<String, String> data) {
//        首先要知道是谁在插入 拿到token
        UsernamePasswordAuthenticationToken authenticationToken =
                (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();

        UserDetailsImpl loginUser = (UserDetailsImpl) authenticationToken.getPrincipal();
        User user = loginUser.getUser();

//        把需要插入的信息取出来 id自增不用管 userid在token
//        data 就是前端页面中 的data 然后各个字符串都与之对应
        String title = data.get("title");
        String description = data.get("description");
        String content = data.get("content");

//        返回结果
        Map<String,String> map = new HashMap<>();

        if(title == null || title.length() == 0){
            map.put("error_message","标题不能为空");
            return map;
        }

        if(title.length() > 100){
            map.put("error_message","标题长度不能长于100");
            return map;
        }

        if(description == null || description.length() == 0){
            description = "这个人很懒";
        }

        if(description.length() >= 300){
            map.put("error_message","描述不能大于300");
            return map;
        }

        if(content == null || content.length() == 0){
            map.put("error_message","代码不能为空");
            return map;
        }

        if(content.length() > 10000){
            map.put("error_message","代码长度过长");
            return map;
        }

        Date now = new Date();
//        如果都合法的话
        Bot bot  = new Bot(null,user.getId(),title, description, content, now, now);

        botMapper.insert(bot);
        map.put("error_message","success");
        return map;
    }
}
