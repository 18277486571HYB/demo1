package com.hyb.demo1.bean;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
public class Msg{

    private Boolean success;


    private Integer code;

    private String message;


    private Map<String,Object> data=new HashMap<>();

    private Msg(){}

    public static Msg success(){
        Msg msg = new Msg();
        msg.setSuccess(true);
        msg.setCode(200);
        msg.setMessage("成功");
        return msg;
    }

    public static Msg fail(){
        Msg msg=new Msg();
        msg.setCode(100);
        msg.setSuccess(false);
        msg.setMessage("失败");
        return msg;
    }

    public Msg success(Boolean b){
        this.setSuccess(false);
        return this;
    }
    public Msg code(Integer code){
        this.setCode(code);
        return this;
    }
    public Msg message(String message){
        this.setMessage(message);
        return this;
    }
    public Msg data(Map<String,Object> map){
        this.setData(map);
        return this;
    }

    public Msg data(String key,Object value){
        this.data.put(key,value);
        return this;
    }

}
