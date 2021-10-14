package finley.gmair.param.membership;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * @Author Joby
 */
@Data
public class IntegralDepositParam {

    @NotBlank
    private String consumerId;
    @NotNull
    private Integer integral;
    @Size(max=100)
    private String description;

}
