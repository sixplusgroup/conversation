package finley.gmair.dto.management;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * @Author Joby
 * @Date 10/19/2021 5:27 PM
 * @Description
 */
@Data
public class MembershipUserDto {
    private String id; // There is different from MembershipUser, the type be change into String. if you user Long, there may loss of accuracy.
    private Integer integral;
    private Integer membershipType;
    private String userMobile;
    private String pic;
    private String nickName;
    private String consumerName;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat( pattern="yyyy-MM-dd HH:mm:ss")
    private Date createTime;
}
