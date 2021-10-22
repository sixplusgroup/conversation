package finley.gmair.service;

import com.baomidou.mybatisplus.extension.service.IService;
import finley.gmair.model.membership.MembershipConfig;

/**
 * @Author Joby
 * @Date 10/20/2021 8:44 PM
 * @Description
 */
public interface MembershipConfigService extends IService<MembershipConfig> {
    Integer getConfigSignUpIntegral();
}
