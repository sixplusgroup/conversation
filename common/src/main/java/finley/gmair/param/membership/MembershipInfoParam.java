package finley.gmair.param.membership;

import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @Author Joby
 * @Date 10/18/2021 6:06 PM
 * @Description
 */
@Data
public class MembershipInfoParam {
    @NotBlank
    private String consumerId;
    @NotBlank
    private String userMobile;
    private String pic;
    private String nickName;
    private String consumerName;
}
