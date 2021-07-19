package finley.gmair.dao;

import finley.gmair.model.membership.MembershipConsumer;
import finley.gmair.util.ResultData;

import java.util.Map;

/**
 * @InterfaceName MembershipDao
 * @Description membership operation interface
 * @Author Joby
 * @Date 2021/7/18 14:49
 */
public interface MembershipDao {
    ResultData insert(MembershipConsumer member);
    ResultData update(Map<String,Object> condition);
    ResultData getOneById(String consumerId);
}
