package finley.gmair.model.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @author ：tsl
 * @date ：Created in 2021-12-20 21:07
 * @description ：统一店铺
 */

@NoArgsConstructor
@AllArgsConstructor
@Data
public class UnifiedShop {
    private String shopId;

    private Integer platform;

    private String sid;

    private String shopTitle;

    private String channel;

    private ShopAuthorizeInfo shopAuthorizeInfo;

    private ShopPullInfo shopPullInfo;

    public void updateLastPullTime(Date date) {
        this.shopPullInfo.setLastPullTime(date);
    }
}
