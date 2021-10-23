package finley.gmair.param.management;

import lombok.Data;


/**
 * @Author Joby
 * @Date 10/19/2021 9:02 PM
 * @Description
 */
@Data
public class IntegralConfirmParam {
    private String id;
    private String consumerId;
    private Boolean isConfirmed;
    private String membershipUserId;
    private String search;
    private Integer membershipType;
    private Boolean blockFlag;
    private String sortType;
}
