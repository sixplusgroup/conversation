package finley.gmair.dao;

import finley.gmair.model.membership.MembershipConsumer;
import finley.gmair.util.ResultData;

/**
 * @InterfaceName MembershipDao
 * @Description 会员操作Dao接口
 * @Author Joby
 * @Date 2021/7/18 14:49
 */
public interface MembershipDao {
    ResultData insert(MembershipConsumer member);
}
