package com.hyb.demo1.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import java.io.Serializable;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 
 * </p>
 *
 * @author hyb
 * @since 2022-06-16 15:40:59
 */
@Getter
@Setter
@TableName("friend")
public class Friend extends Model<Friend> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @TableId(value = "main_name")
    private String mainName;

    @TableField("friend_name")
    private String friendName;

    @TableField("friend_ip")
    private String friendIp;

    @TableField("status")
    private Integer status;


    @Override
    public Serializable pkVal() {
        return this.id;
    }

}
