package finley.gmair.param.membership;

import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * @Author Joby
 */
@Data
public class SupplementaryIntegralParam {
    @NotNull
    private String consumerId;
    @NotNull
    private String deviceModel;
    @Size(max=80)
    private String description;
    @NotNull
    private String pictures;
}
