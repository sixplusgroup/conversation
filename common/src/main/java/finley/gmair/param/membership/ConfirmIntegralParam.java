package finley.gmair.param.membership;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import finley.gmair.util.jsonSerialize.StringToLong;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @Author Joby
 */
@Data
public class ConfirmIntegralParam {
    @NotNull
    @JsonDeserialize(using= StringToLong.class)
    private Long id;
}
