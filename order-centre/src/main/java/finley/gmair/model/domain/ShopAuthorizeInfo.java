package finley.gmair.model.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @author ：tsl
 * @date ：Created in 2021-12-21 16:01
 * @description ：店铺授权信息
 */

@NoArgsConstructor
@AllArgsConstructor
@Data
public class ShopAuthorizeInfo {
    private String appKey;

    private String appSecret;

    private String sessionKey;

    private Date authorizeTime;

    private Date expireTime;
}
