package finley.gmair.model.ordernew;

import finley.gmair.model.Entity;
import lombok.Data;

import java.util.Date;

/**
 * @author ：tsl
 * @date ：Created in 2020/10/17 15:26
 * @description ：user info of taobao seller
 */

@Data
public class TbUser extends Entity {
    private String user_id;
    private Date startSyncTime;
    private Date lastUpdateTime;
    private String sessionKey;
    private Date authorizeTime;
}
