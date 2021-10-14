package finley.gmair.param.membership;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * @Author Joby
 */
@Data
public class SupplementaryIntegralParam {
    @NotBlank
    private String consumerId;
    @NotBlank
    private String deviceModel;
    @Size(max=80)
    private String description;
    @NotBlank
    private String pictures;
}
