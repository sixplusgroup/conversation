package finley.gmair.dto.installation;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * @Author Joby
 */
@Data
public class IntegralConfirmDto {
    private String id;
    private Integer integralValue;
    private Boolean isConfirmed;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat( pattern="yyyy-MM-dd HH:mm:ss")
    private Date createTime;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat( pattern="yyyy-MM-dd HH:mm:ss")
    private Date confirmedTime;
    private String description;
    private String deviceModel;
    private String pictures;

    private String membershipUserId;
    private String userMobile;
    private String consumerName;
    private Integer membershipIntegral;
    private Integer membershipType;

}
