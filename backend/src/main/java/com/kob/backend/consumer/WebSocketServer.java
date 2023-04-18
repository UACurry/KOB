package com.kob.backend.consumer;

import com.kob.backend.mapper.UserMapper;
import com.kob.backend.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;

// 每次来一个链接 就new一个这个链接的实例
@Component
@ServerEndpoint("/websocket/{token}")  // 注意不要以'/'结尾
public class WebSocketServer {

//    维护全局 链接 websocket  ConcurrentHashMap 线程安全的hash表
//    static 所有实例均可见 公用 在每个实例里面 都要访问同一个hash表
    private static ConcurrentHashMap<Integer,WebSocketServer> users = new ConcurrentHashMap<>();

//    存储用户信息 用于存储这个链接是哪个用户的
    private User user;

//    用于后端向前端发送 每个链接用session来维护
    private Session session = null;

//    注入数据库
    private static UserMapper userMapper;

    @Autowired
    public void setUserMapper(UserMapper userMapper){
//        静态变量访问时候 用类名来访问
        WebSocketServer.userMapper = userMapper;
    }

    @OnOpen
//    链接建立时候 自动触发
    public void onOpen(Session session, @PathParam("token") String token) {
        // 建立连接
        this.session = session;
        System.out.println("connected");
    }

    @OnClose
    public void onClose() {
        // 关闭链接
        System.out.println("disconnected");
    }

//    当前端向后端发送信息 所有逻辑都在这里
    @OnMessage
    public void onMessage(String message, Session session) {
        // 从Client接收消息
        System.out.println("recive");
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
