package finley.gmair.dto.membership;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * @Author Joby
 */
@Data
public class IntegralRecordDto {
    private Integer integralValue;
    private Boolean isAdd;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat( pattern="yyyy-MM-dd HH:mm:ss")
    private Date createTime;
    private String description;

    private String membershipUserId;
    private String userMobile;
    private String consumerName;
    private Integer membershipIntegral;
    private Integer membershipType;

}
