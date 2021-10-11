package finley.gmair.model;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

/**
 * @Author Joby
 */
@Data
@TableName("tz_membership_user")
public class MembershipUser implements Serializable {
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    private Integer integral;
    private Integer membershipType= 0;
    private String consumerId;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime = new Date();

}
