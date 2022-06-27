package com.hyb.demo1.bean;

import lombok.Data;

import java.io.Serializable;
import java.util.concurrent.atomic.AtomicInteger;

/*
* 消息实体
* */
@Data
public class Message implements Serializable {

    private static final long serialVersionUID = 1L;

    //发送给指定的uuid,
    private String uuid;
    private String user;
    private String message;
    private String date;
    private String ip;
    private AtomicInteger nums;
    private String currentGroup;

}
