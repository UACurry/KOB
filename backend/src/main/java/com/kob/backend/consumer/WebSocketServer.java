package com.kob.backend.consumer;

import com.alibaba.fastjson.JSONObject;
import com.kob.backend.consumer.utils.Game;
import com.kob.backend.consumer.utils.JwtAuthentication;
import com.kob.backend.mapper.UserMapper;
import com.kob.backend.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.Iterator;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;

// 每次来一个链接 就new一个这个链接的实例
@Component
@ServerEndpoint("/websocket/{token}")  // 注意不要以'/'结尾
public class WebSocketServer {

//    维护全局 链接 websocket  ConcurrentHashMap 线程安全的hash表
//    static 所有实例均可见 公用 在每个实例里面 都要访问同一个hash表
    private static final ConcurrentHashMap<Integer,WebSocketServer> users = new ConcurrentHashMap<>();

//    开一个匹配池 也要用线程安全的  CopyOnWriteArraySet 就是线程安全的的
    private static final CopyOnWriteArraySet<User> matchpool = new CopyOnWriteArraySet<>();

//    存储用户信息 用于存储这个链接是哪个用户的
    private User user;

//    用于后端向前端发送 每个链接用session来维护
    private Session session = null;

//    注入数据库
    private static UserMapper userMapper;

//    存地图


    @Autowired
    public void setUserMapper(UserMapper userMapper){
//        静态变量访问时候 用类名来访问
        WebSocketServer.userMapper = userMapper;
    }

    @OnOpen
//    链接建立时候 自动触发
    public void onOpen(Session session, @PathParam("token") String token) throws IOException {
        // 建立连接
        this.session = session;
        System.out.println("connected");
//        JwtAuthentication 自己封装好的一个工具类 用于解析token
        Integer userId = JwtAuthentication.getUserId(token);
        this.user = userMapper.selectById(userId);

        if (this.user != null) {
            users.put(userId, this);
        } else {
            this.session.close();
        }

        System.out.println(users);

    }

    @OnClose
    public void onClose() {
        // 关闭链接
        System.out.println("disconnected");
        if(this.user != null){
            users.remove(this.user.getId());
            matchpool.remove(this.user);
        }
    }

    private void startMatching(){
        System.out.println("startMatching");
        matchpool.add(this.user);

        while(matchpool.size() >= 2){
//            用一个迭代器
            Iterator<User> it = matchpool.iterator();
            User a = it.next(),b = it.next();
            matchpool.remove(a);
            matchpool.remove(b);

//
            Game game = new Game(13, 14, 20);
            game.createMap();


//            respA 是匹配成功的链接 不一定是 A 和 B 的链接
            JSONObject respA = new JSONObject();
            respA.put("event", "is-matched");
            respA.put("opponent_username", b.getUsername());
            respA.put("opponent_photo", b.getPhoto());
            respA.put("gamemap", game.getG());
//            用user.get 来获取 A 的 websocket链接
            users.get(a.getId()).SendMessage(respA.toJSONString());

            JSONObject respB = new JSONObject();
            respB.put("event", "is-matched");
            respB.put("opponent_username", a.getUsername());
            respB.put("opponent_photo", a.getPhoto());
            respB.put("gamemap", game.getG());
//            用user.get 来获取 A 的 websocket链接
            users.get(b.getId()).SendMessage(respB.toJSONString());

        }
    }

    private void stopMatching(){
        System.out.println("startMatching");
        matchpool.remove(this.user);
    }

//    当前端向后端发送信息 所有逻辑都在这里
    @OnMessage
    public void onMessage(String message, Session session) { // 当作一个路由
        // 从Client接收消息
        System.out.println("recive");
//        解析message
        JSONObject data = JSONObject.parseObject(message);

//        解析完之后 取出event  event就是前端定义好的 一个字典的键
        String event = data.getString("event");

//        前端中 matchground 里面写好的
        if("start-matching".equals(event)){
            startMatching();
        } else if ("start-matching".equals(event)) {
            stopMatching();
        }

    }

    @OnError
    public void onError(Session session, Throwable error) {
        error.printStackTrace();
    }

//    从后端向前端发送
    public void  SendMessage(String message){
//        异步通信 加锁
        synchronized (this.session){
            try{
//                可以将信息发送到前端
                this.session.getBasicRemote().sendText(message);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
