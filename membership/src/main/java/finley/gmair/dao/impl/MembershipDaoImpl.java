package finley.gmair.dao.impl;

import finley.gmair.dao.BaseDao;
import finley.gmair.dao.MembershipDao;
import finley.gmair.model.membership.MembershipConsumer;
import finley.gmair.util.ResponseCode;
import finley.gmair.util.ResultData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

/**
 * @ClassName MembershipDaoImpl
 * @Description 会员操作Dao实现类
 * @Author Joby
 * @Date 2021/7/18 14:52
 */
@Repository
public class MembershipDaoImpl extends BaseDao implements MembershipDao {
    private Logger logger = LoggerFactory.getLogger(MembershipDaoImpl.class);
    @Override
    public ResultData insert(MembershipConsumer member) {
        ResultData result = new ResultData();
        try{
            sqlSession.insert("gmair.membership.membership.insert",member);
            result.setData(member);
        }catch (Exception e){
            logger.error(e.getMessage());
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription(e.getMessage());
        }
        return result;
    }
}
