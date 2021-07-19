package finley.gmair.dao.impl;

import finley.gmair.dao.BaseDao;
import finley.gmair.dao.MembershipDao;
import finley.gmair.model.membership.IntegralAdd;
import finley.gmair.model.membership.MembershipConsumer;
import finley.gmair.util.ResponseCode;
import finley.gmair.util.ResultData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.util.Map;

/**
 * @ClassName MembershipDaoImpl
 * @Description the implementation class of membership Dao interface
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

    @Override
    public ResultData getOneById(String consumerId) {
        ResultData result = new ResultData();
        try{
            MembershipConsumer membershipConsumer = sqlSession.selectOne("gmair.membership.membership.selectOne",consumerId);
            if(membershipConsumer==null){
                result.setResponseCode(ResponseCode.RESPONSE_ERROR);
                result.setDescription("can not find the member");
            }else{
                result.setData(membershipConsumer);
            }
        }catch(Exception e){
            logger.error(e.getMessage());
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("the `get` operation failed");
        }
        return result;
    }

    @Override
    public ResultData update(Map<String, Object> condition) {
        ResultData result = new ResultData();
        try{
            sqlSession.update("gmair.membership.membership.update",condition);
        }catch(Exception e){
            logger.error(e.getMessage());
            result.setDescription(e.getMessage());
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            return result;
        }
        return result;
    }

    @Override
    public ResultData setEventScheduler() {
        ResultData result = new ResultData();
        try{
            sqlSession.update("gmair.membership.membership.setEventScheduler");
        }catch(Exception e){
            logger.error(e.getMessage());
            result.setDescription(e.getMessage());
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            return result;
        }
        return result;
    }
}
