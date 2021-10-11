package finley.gmair.bean.param;

import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * @Author Joby
 */
@Data
public class IntegralWithdrawParam {

    @NotNull
    private String consumerId;
    @NotNull
    private Integer integral;
    @Size(max=80)
    private String description;

}
