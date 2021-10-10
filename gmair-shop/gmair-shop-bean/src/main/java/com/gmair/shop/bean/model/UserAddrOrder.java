

package com.gmair.shop.bean.model;

import java.io.Serializable;
import java.util.Date;
import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

@Data
@TableName("tz_user_addr_order")
public class UserAddrOrder implements Serializable {
    /**
     * ID
     */
    @TableId


    private Long addrOrderId;

    /**
     * 地址ID
     */

    private Long addrId;

    /**
     * 用户ID
     */

    private String userId;

    /**
     * 收货人
     */
    private String receiver;

    /**
     * 省
     */

    private String province;
    
    /**
     * 城市
     */

    private String city;
    
    /**
     * 区
     */

    private String area;

    /**
     * 地址
     */
    private String addr;

    /**
     * 邮编
     */

    private String postCode;

    /**
     * 省ID
     */

    private Long provinceId;
    
    /**
     * 城市ID
     */

    private Long cityId;
    
    /**
     * 区域ID
     */

    private Long areaId;

    /**
     * 手机
     */
    private String mobile;

    /**
     * 建立时间
     */

    private Date createTime;

    /**
     * 版本号
     */
    private Integer version;

}