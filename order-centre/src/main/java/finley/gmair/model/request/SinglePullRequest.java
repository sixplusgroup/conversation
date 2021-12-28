package finley.gmair.model.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author ：tsl
 * @date ：Created in 2021-12-21 15:04
 * @description ：
 */

@NoArgsConstructor
@AllArgsConstructor
@Data
public class SinglePullRequest {
    private String shopId;

    private String tid;
}
