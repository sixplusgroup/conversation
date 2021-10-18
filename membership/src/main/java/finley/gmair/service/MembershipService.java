package finley.gmair.service;

import com.baomidou.mybatisplus.extension.service.IService;
import finley.gmair.model.membership.MembershipUser;


/**
 * @Author Joby
 */
public interface MembershipService extends IService<MembershipUser> {

    Boolean createMembership(String consumerId);

    MembershipUser getMembershipById(Long membershipId);

    MembershipUser getMembershipByConsumerId(String consumerId);

    Long getMembershipIdByConsumerId(String consumerId);

    void addIntegral(Long membershipId, Integer integralValue);

    void withdrawIntegralById(Long membershipId, Integer integral);

    void updateMembership(MembershipUser membershipUser);

}
