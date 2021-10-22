package finley.gmair.param.installation;

import lombok.Data;


/**
 * @Author Joby
 * @Date 10/19/2021 9:02 PM
 * @Description
 */
@Data
public class IntegralRecordParam {
    private Boolean isAdd;
    private String membershipUserId;
    private String search;
    private Integer membershipType;
    private Boolean blockFlag;
    private String sortType;
}
