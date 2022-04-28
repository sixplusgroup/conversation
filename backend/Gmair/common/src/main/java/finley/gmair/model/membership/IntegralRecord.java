package finley.gmair.model.membership;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import finley.gmair.model.Entity;
import finley.gmair.model.EntityPlus;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * @Author Joby
 */
@Data
@TableName("tz_integral_record")
public class IntegralRecord extends EntityPlus {
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;
    private Long membershipUserId;
    private Integer integralValue;
    private Boolean isAdd = false;

    private String description;
}
