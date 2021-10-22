package finley.gmair.model.membership;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import finley.gmair.model.Entity;
import finley.gmair.model.EntityPlus;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

/**
 * @Author Joby
 */
@Data
@TableName("tz_integral_add")
public class IntegralAdd extends EntityPlus implements Serializable {
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;
    private Long membershipUserId;
    private Integer integralValue;
    private Boolean isConfirmed=false;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date confirmedTime;
    private String description;
    private String deviceModel;
    private String pictures;
}
