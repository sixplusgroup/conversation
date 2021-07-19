package finley.gmair.service;

import finley.gmair.model.membership.IntegralAdd;
import finley.gmair.util.ResultData;

/**
 * @InterfaceName MembershipService
 * @Description
 * @Author Joby
 * @Date 2021/7/16 17:27
 */
public interface MembershipService {
    ResultData create(String consumerId);
    boolean checkMemberIsValid(String consumerId);
}
