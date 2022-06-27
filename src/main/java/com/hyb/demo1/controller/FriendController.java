package com.hyb.demo1.controller;


import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.hyb.demo1.bean.Msg;
import com.hyb.demo1.entity.Friend;
import com.hyb.demo1.handler.MyWebSocket;
import com.hyb.demo1.service.FriendService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author hyb
 * @since 2022-06-16 15:34:50
 */
@RestController
@RequestMapping("/demo1/friend")
public class FriendController {

    @Autowired
    FriendService friendService;

    @Autowired
    MyWebSocket webSocket;


    @PostMapping("/save")
    public Msg addFriend(@RequestBody Friend friend){
        friendService.save(friend);

        return Msg.success().data("friend",friend);
    }
    @GetMapping("/get/{id}")
    public Msg get(@PathVariable("id")String id){
        QueryWrapper<Friend> friendQueryWrapper = new QueryWrapper<>();
        friendQueryWrapper.eq("main_name",id);
        List<Friend> list = friendService.list(friendQueryWrapper);
        return Msg.success().data("friends",list);
    }

    @GetMapping("/del/{user1}/{user2}")
    public Msg del(@PathVariable("user1")String user1,@PathVariable("user2")String user2){
        QueryWrapper<Friend> friendQueryWrapper = new QueryWrapper<>();
        friendQueryWrapper.eq("main_name",user1);
        friendQueryWrapper.eq("friend_name",user2);
        friendService.remove(friendQueryWrapper);
        return Msg.success();
    }

}

