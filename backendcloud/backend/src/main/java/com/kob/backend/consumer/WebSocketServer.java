package com.kob.backend.consumer;

import com.alibaba.fastjson.JSONObject;
import com.kob.backend.consumer.utils.Game;
import com.kob.backend.consumer.utils.JwtAuthentication;
import com.kob.backend.mapper.RecordMapper;
import com.kob.backend.mapper.UserMapper;
import com.kob.backend.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

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
//    users 存储所有玩家的 websocket链接
    public static final ConcurrentHashMap<Integer,WebSocketServer> users = new ConcurrentHashMap<>();

//    开一个匹配池 也要用线程安全的  CopyOnWriteArraySet 就是线程安全的的
//    private static final CopyOnWriteArraySet<User> matchpool = new CopyOnWriteArraySet<>();

//    存储用户信息 用于存储这个链接是哪个用户的
    private User user;

//    用于后端向前端发送 每个链接用session来维护
    private Session session = null;

//    注入数据库
    private static UserMapper userMapper;

    public static RecordMapper recordMapper;

    private static RestTemplate restTemplate;

    private Game game = null;

    private final static String addPlayerUrl = "http://127.0.0.1:1792/player/add/";
    private final static String removePlayerurl = "http://127.0.0.1:1792/player/remove/";


//    存地图


    @Autowired
    public void setUserMapper(UserMapper userMapper){
//        静态变量访问时候 用类名来访问
        WebSocketServer.userMapper = userMapper;
    }

    @Autowired
    public void setRecordMapper(RecordMapper recordMapper){
        WebSocketServer.recordMapper = recordMapper;
    }

    @Autowired
    public void setRestTemplate(RestTemplate restTemplate){
        WebSocketServer.restTemplate = restTemplate;
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
//            matchpool.remove(this.user);
        }
    }

//    public 在外面能调用它 static 能够通过类名来调用
    public static void startGame(Integer aId, Integer bId){
        User a = userMapper.selectById(aId);
        User b = userMapper.selectById(bId);

        //            初始化传入构造参数
        Game game = new Game(13, 14, 20, a.getId(), b.getId());
        game.createMap();
//            是 Thread 函数的一个api 可以另起一个线程来执行这个函数
        game.start();

//            把game 赋给两名玩家 users.get 可以获取 a 的websocket链接
//        if (users.get(a.getId()) != null)  防止有玩家将网页关掉后 获取的id就为空了
        if (users.get(a.getId()) != null)
            users.get(a.getId()).game = game;
        if (users.get(b.getId()) != null)
            users.get(b.getId()).game = game;



//            存放game全局信息的
        JSONObject respGame = new JSONObject();
        respGame.put("a_id",game.getPlayerA().getId());
        respGame.put("a_sx", game.getPlayerA().getSx());
        respGame.put("a_sy", game.getPlayerA().getSy());

        respGame.put("b_id",game.getPlayerB().getId());
        respGame.put("b_sx", game.getPlayerB().getSx());
        respGame.put("b_sy", game.getPlayerB().getSy());

//            把地图传过去
        respGame.put("map",game.getG());


//            respA 是匹配成功的链接 不一定是 A 和 B 的链接
        JSONObject respA = new JSONObject();
        respA.put("event", "is-matched");
        respA.put("opponent_username", b.getUsername());
        respA.put("opponent_photo", b.getPhoto());
//            通过此 把两名玩家信息传过去 同时也能获得地图
        respA.put("game", respGame);
//            用user.get 来获取 A 的 websocket链接
        if (users.get(a.getId()) != null)
            users.get(a.getId()).SendMessage(respA.toJSONString());

        JSONObject respB = new JSONObject();
        respB.put("event", "is-matched");
        respB.put("opponent_username", a.getUsername());
        respB.put("opponent_photo", a.getPhoto());
        respB.put("game", respGame);
//            用user.get 来获取 A 的 websocket链接
        if (users.get(b.getId()) != null)
            users.get(b.getId()).SendMessage(respB.toJSONString());
    }

    private void startMatching(){
        System.out.println("startMatching");

//        开始匹配时候 向Matching system 发送请求
        MultiValueMap<String, String> data = new LinkedMultiValueMap<>();
        data.add("user_id", this.user.getId().toString());
        data.add("rating", this.user.getRating().toString());
//        访问一下 addPlayerUrl 请求 向Matching system 发送请求
        restTemplate.postForObject(addPlayerUrl, data, String.class);

//        matchpool.add(this.user);

////        傻瓜匹配 只要有两个人 就可以配对
//        while(matchpool.size() >= 2){
////            用一个迭代器
//            Iterator<User> it = matchpool.iterator();
//            User a = it.next(),b = it.next();
//            matchpool.remove(a);
//            matchpool.remove(b);
//
//        }
    }

    private void stopMatching(){
        System.out.println("stopMatching");
//        matchpool.remove(this.user);

//        向matching system
        MultiValueMap<String, String> data = new LinkedMultiValueMap<>();
        data.add("user_id", this.user.getId().toString());
//        向后端发送请求
        restTemplate.postForObject(removePlayerurl, data, String.class);
    }

    private void move(int direction){
//        判断是哪个用的操作
        if(game.getPlayerA().getId().equals(user.getId())){
            game.setNextStepA(direction);
        } else if(game.getPlayerB().getId().equals(user.getId())){
            game.setNextStepB(direction);
        }
    }


//    当前端向后端发送信息 所有逻辑都在这里
    @OnMessage
    public void onMessage(String message, Session session) { // 当作一个路由
        // 从Client接收消息
        System.out.println("recive");
//        解析message
        JSONObject data = JSONObject.parseObject(message);

//        解析完之后 取出event的值  event就是前端定义好的 一个字典的键
        String event = data.getString("event");

//        前端中 matchground 里面写好的 前端中定义的 event时间
        if("start-matching".equals(event)){
            startMatching();
        } else if ("stop-matching".equals(event)) {
            stopMatching();
        } else if ("move".equals(event)) {
            move(data.getInteger("direction"));
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
