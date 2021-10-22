package finley.gmair.param.membership;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import finley.gmair.util.jsonSerialize.StringToLong;
import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * @Author Joby
 */
@Data
public class GiveIntegralParam {
    @NotNull
    @JsonDeserialize(using= StringToLong.class)
    private Long id;
    @NotNull
    private Integer integralValue;

}
