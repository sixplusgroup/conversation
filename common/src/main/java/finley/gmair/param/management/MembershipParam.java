package finley.gmair.param.management;

import lombok.Data;

/**
 * @Author Joby
 * @Date 10/18/2021 9:09 PM
 * @Description
 */
@Data
public class MembershipParam {
    private Long id;
    private Integer membershipType;
    private String userMobile;
    private String consumerName;
}
