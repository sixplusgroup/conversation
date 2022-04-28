package finley.gmair.model.membership;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * @Author Joby
 * @Date 10/20/2021 8:39 PM
 * @Description
 */
@Data
@TableName("tz_membership_config")
public class MembershipConfig {
    private String configName;
    private String configValue;
}
