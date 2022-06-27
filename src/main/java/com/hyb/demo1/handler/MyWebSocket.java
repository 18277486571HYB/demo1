package com.hyb.demo1.handler;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.hyb.demo1.bean.Message;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;


@Slf4j
@ServerEndpoint(value = "/websocket/{user}/{ip}")
@Component
public class MyWebSocket {


    // 通过类似GET请求方式传递参数的方法（服务端采用第二种方法"WebSocketHandler"实现）
//    websocket = new WebSocket("ws://127.0.0.1:18080/testWebsocket?id=23&name=Lebron");
    /**
     * 在线人数
     */
    public static AtomicInteger onlineNumber = new AtomicInteger(0);

    /**
     * 所有的对象，每次连接建立，都会将我们自己定义的MyWebSocket存放到List中，
     */
    public static List<MyWebSocket> webSockets = new CopyOnWriteArrayList<MyWebSocket>();

    /**
     * 会话，与某个客户端的连接会话，需要通过它来给客户端发送数据
     */
    private Session session;

    /**
     * 每个会话的用户
     */
    private String user;


    /*
    * 每个用户的ip
    * */
    private String ip;

    /**
     * 建立连接
     *
     * @param session
     */
    @OnOpen
    public void onOpen(Session session, @PathParam("user") String user,@PathParam("ip") String ip) throws IOException {
        //如果用户为空
        if (user == null || "".equals(user) || ip==null|| "".equals(ip)) {
            try {
                session.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return;
        }
        //增加在线人数
        onlineNumber.incrementAndGet();
        for (MyWebSocket myWebSocket : webSockets) {
            if (user.equals(myWebSocket.user)) {
                try {
                    session.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                return;
            }
        }
        this.session = session;
        this.user = user;
        this.ip=ip;
        webSockets.add(this);
        //日志记录
        log.info("有新连接加入！ 当前在线人数:{}",onlineNumber.get());
        //发送在线人数消息
        Message message = new Message();
        message.setUser(" ");
        message.setMessage(" ");
        message.setNums(onlineNumber);
        message.setIp(ip);
        pushMessage(message);
    }

    /**
     * 连接关闭
     */
    @OnClose
    public void onClose() {
        //减少在线人数
        onlineNumber.decrementAndGet();
        //移除该在线实例的socket
        webSockets.remove(this);
        log.error("有连接关闭！ 当前在线人数:{}",onlineNumber.get());
    }

    /**
     * 收到客户端的消息
     *
     * @param message 消息
     * @param session 会话
     */
    @OnMessage
    public void onMessage(String message, Session session, @PathParam("user") String user,@PathParam("ip") String ip) {
        //转换成json格式
        JSONObject jsonObject = JSON.parseObject(message);
        String msg = (String) jsonObject.get("message");
        String uuid = (String) jsonObject.get("uuid");
        String user1 = (String) jsonObject.get("user");
        String currentGroup=(String) jsonObject.get("currentGroup");
        log.info("来自[{}]消息:{}",user,msg);
        Message message1 = new Message();
        if ("系统".equals(user1)) {
            message1.setUser(user1);
        } else {
            message1.setUser(user);
        }
        message1.setIp(ip);
        message1.setMessage(msg);
        message1.setUuid(uuid);
        //格式化时间
        message1.setDate(new SimpleDateFormat("yy-MM-dd hh:mm:ss").format(new Date()));
        message1.setNums(onlineNumber);
        //设置当前群聊组
        message1.setCurrentGroup(currentGroup);
        //发送消息
        pushMessage(message1);
    }

    /**
     * 发送消息
     *
     * @param message 消息
     */
    public void sendMessage(String message) {
        try {
            //发送非json格式的消息
            session.getBasicRemote().sendText(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendMessage(Message message) {
        try {
            //发送json格式的消息
            session.getBasicRemote().sendText(JSON.toJSONString(message));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 消息推送
     *
     * @param message
     * @param uuid    uuid为空则推送全部人员
     */
    public static void pushMessage(String user, String message, String uuid,String ip) {
        if (uuid == null || "".equals(uuid)) {
            for (MyWebSocket myWebSocket : webSockets) {
                myWebSocket.sendMessage(user + ":" + message +" "+new SimpleDateFormat("yy-MM-dd hh:mm:ss").format(new Date())+"来自:"+ip);
            }
        } else {
            for (MyWebSocket myWebSocket : webSockets) {
                if (uuid.equals(myWebSocket.user)) {
                    myWebSocket.sendMessage(message);
                }
            }
        }

    }

    public static void pushMessage(Message message){
        //如果uuid为空,就推送给所有人,说明是群
        if (message.getUuid() == null || "".equals(message.getUuid())) {
            for (MyWebSocket myWebSocket : webSockets) {
                myWebSocket.sendMessage(message);
            }

        } else {
            //如果uuid不为空
            //让当前socket发送一次消息
            //让目的socket发送一次消息

            for (MyWebSocket myWebSocket : webSockets) {

                if (message.getUuid().equals(myWebSocket.user) || message.getUser().equals(myWebSocket.user)) {

                    myWebSocket.sendMessage(message);
                }

            }

        }
    }

    //获取session对象
    public Session getSession(){
        return this.session;
    }
    //获取ip
    public String getIp(){
        return this.ip;
    }
    public AtomicInteger getOnlineNumber(){
        return onlineNumber;
    }


}

