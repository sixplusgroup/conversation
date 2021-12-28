package finley.gmair.model.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @author ：tsl
 * @date ：Created in 2021-12-21 16:03
 * @description ：
 */

@NoArgsConstructor
@AllArgsConstructor
@Data
public class ShopPullInfo {
    private Date startPullTime;

    private Date lastPullTime;
}
