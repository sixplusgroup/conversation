package finley.gmair.dao.impl;

import finley.gmair.dao.BaseDao;
import finley.gmair.dao.IntegralDao;
import finley.gmair.model.membership.IntegralAdd;
import finley.gmair.util.IDGenerator;
import finley.gmair.util.ResponseCode;
import finley.gmair.util.ResultData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;


/**
 * @ClassName: IntegralDaoImpl
 * @Description: TODO
 * @Author fan
 * @Date 2021/7/13 2:59 PM
 */

@Repository
public class IntegralDaoImpl extends BaseDao implements IntegralDao {
    private Logger logger = LoggerFactory.getLogger(IntegralDaoImpl.class);

    @Override
    public ResultData insert(IntegralAdd integralAdd) {
        ResultData result = new ResultData();
        integralAdd.setAddId(IDGenerator.generate("IGA"));
        try{
            sqlSession.insert("gmair.membership.integral.insert",integralAdd);
        }catch (Exception e){
            logger.error(e.getMessage());
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("e.getMessage()");
        }
        return result;
    }

    @Override
    public ResultData getOneById(String addId) {
        ResultData result = new ResultData();
        try{
            IntegralAdd integralAdd = sqlSession.selectOne("gmair.membership.integral.selectOne",addId);
            if(integralAdd==null){
                result.setResponseCode(ResponseCode.RESPONSE_ERROR);
                result.setDescription("can not find the integral record");
            }else{
                result.setData(integralAdd);
            }
        }catch(Exception e){
            logger.error(e.getMessage());
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("the `get` operation failed");
        }
        return result;
    }

    @Override
    public ResultData updateConfirm(String addId) {
        ResultData result = new ResultData();
        try{
            sqlSession.update("gmair.membership.integral.updateConfirm",addId);
        }catch(Exception e){
            logger.error(e.getMessage());
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("the `updateConfirm` operation failed");
        }
        return result;
    }
}
