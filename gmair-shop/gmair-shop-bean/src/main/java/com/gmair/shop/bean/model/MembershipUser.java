package com.gmair.shop.bean.model;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import com.gmair.shop.bean.enums.MembershipType;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

/**
 * @Author Joby
 */
@Data
@TableName("tz_membership_user")
public class MembershipUser implements Serializable {
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;
    private String userId;
    private Integer integral;
    private Integer membershipType= 0;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime = new Date();

}
