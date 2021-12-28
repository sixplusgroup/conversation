package finley.gmair.model.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @author ：tsl
 * @date ：Created in 2021-12-25 23:39
 * @description ：
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TradeQuery {
    private String tid;

    private Integer platform;

    private String shopId;

    private Integer status;

    private Date startTime;

    private Date endTime;

    private String phone;
}
