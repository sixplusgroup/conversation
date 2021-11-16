package finley.gmair.param.membership;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * @Author Joby
 */
@Data
public class IntegralWithdrawParam {

    @NotBlank
    private String consumerId;
    @NotNull
    private Integer integral;
    @Size(max=80)
    private String description;

    public IntegralWithdrawParam(){

    }

    public IntegralWithdrawParam(String consumerId, Integer integral, String description) {
        this.consumerId = consumerId;
        this.integral = integral;
        this.description = description;
    }
}
