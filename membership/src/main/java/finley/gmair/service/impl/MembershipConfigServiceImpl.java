package finley.gmair.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import finley.gmair.dao.MembershipConfigMapping;
import finley.gmair.model.membership.MembershipConfig;
import finley.gmair.service.MembershipConfigService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * @Author Joby
 * @Date 10/20/2021 8:45 PM
 * @Description
 */
@Service
@AllArgsConstructor
public class MembershipConfigServiceImpl extends ServiceImpl<MembershipConfigMapping, MembershipConfig> implements MembershipConfigService{

    private final MembershipConfigMapping membershipConfigMapping;

    @Override
    public Integer getConfigSignUpIntegral() {
        MembershipConfig membershipConfig =  membershipConfigMapping.selectOne(new LambdaQueryWrapper<MembershipConfig>().eq(MembershipConfig::getConfigName,"sign_in_integral"));
        if(membershipConfig==null){
            return 100;
        }
        return Integer.valueOf(membershipConfig.getConfigValue());
    }
}
