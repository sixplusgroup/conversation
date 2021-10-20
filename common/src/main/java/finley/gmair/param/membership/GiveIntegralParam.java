package finley.gmair.param.membership;

import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * @Author Joby
 */
@Data
public class GiveIntegralParam {
    @NotNull
    private Long id;
    @NotNull
    private Integer integralValue;

}
