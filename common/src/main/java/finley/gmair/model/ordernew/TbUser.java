package finley.gmair.model.ordernew;

import finley.gmair.model.Entity;
import lombok.Data;

import java.util.Date;

/**
 * @author ：tsl
 * @date ：Created in 2020/10/17 15:26
 * @description ：user info of taobao seller（淘宝卖家用户表）
 */

@Data
public class TbUser extends Entity{
    /**
     * 主键
     */
    private String userId;

    /**
     * 同步开始时间
     */
    private Date startSyncTime;

    /**
     * 上次同步时间
     */
    private Date lastUpdateTime;

    /**
     * 用户授权token
     */
    private String sessionKey;

    /**
     * 用户授权时间
     */
    private Date authorizeTime;
}
