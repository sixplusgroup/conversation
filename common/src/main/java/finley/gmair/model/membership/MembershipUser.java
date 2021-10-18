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
@TableName("tz_membership_user")
public class MembershipUser extends EntityPlus implements Serializable {
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    private Integer integral;
    private Integer membershipType= 0;
    private String consumerId;



}
