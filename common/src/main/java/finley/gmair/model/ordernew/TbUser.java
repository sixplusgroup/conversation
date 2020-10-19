package finley.gmair.model.ordernew;

import lombok.Data;

import java.util.Date;

/**
 * @author ：tsl
 * @date ：Created in 2020/10/17 15:26
 * @description ：user info of taobao seller
 */

@Data
public class TbUser {
    private Integer id;
    private Date startSyncTime;
    private Date lastUpdateTime;
    private String sessionKey;
    private Long authorizeTime;
}
