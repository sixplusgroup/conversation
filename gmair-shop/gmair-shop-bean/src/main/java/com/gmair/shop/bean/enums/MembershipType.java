package com.gmair.shop.bean.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;

public enum MembershipType {
    ORDINARY(0),
    SILVER(1);


    private Integer num;

    MembershipType(int num){
        this.num = num;
    }
    public Integer value(){
        return num;
    }

}
