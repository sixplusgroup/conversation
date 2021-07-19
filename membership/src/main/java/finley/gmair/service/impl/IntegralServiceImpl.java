package finley.gmair.service.impl;

import finley.gmair.dao.IntegralDao;
import finley.gmair.dao.MembershipDao;
import finley.gmair.model.membership.IntegralAdd;
import finley.gmair.model.membership.MembershipConsumer;
import finley.gmair.service.IntegralService;
import finley.gmair.util.ResponseCode;
import finley.gmair.util.ResultData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

/**
 * @ClassName: IntegralServiceImpl
 * @Description: TODO
 * @Author fan
 * @Date 2021/7/13 2:56 PM
 */
@Service
public class IntegralServiceImpl implements IntegralService {
    private Logger logger = LoggerFactory.getLogger(IntegralServiceImpl.class);
    @Autowired
    IntegralDao integralDao;
    @Autowired
    MembershipDao membershipDao;
    @Override
    public ResultData addIntegral(IntegralAdd integralAdd) {
        return integralDao.insert(integralAdd);
    }

    @Override
    public boolean checkIntegralIsValid(String addId) {
        ResultData response = integralDao.getOneById(addId);
        if(response.getResponseCode()== ResponseCode.RESPONSE_ERROR||response.getData()==null){
            return false;
        }
        IntegralAdd integralAdd = (IntegralAdd)response.getData();
        return !integralAdd.isConfirmed();
    }

    @Override
    public ResultData confirmIntegral(String addId,String consumerId) {
        ResultData result = new ResultData();
        ResultData response = integralDao.getOneById(addId);
        IntegralAdd integralAdd;
        if(response.getResponseCode()== ResponseCode.RESPONSE_ERROR||response.getData()==null){
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("confirm integral failed");
            return result;
        }else{
            integralAdd = (IntegralAdd) response.getData();
        }
        Integer integral = integralAdd.getIntegralValue();
        if(integral<0){
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("confirm integral failed");
            return result;
        }

        response = membershipDao.getOneById(consumerId);
        MembershipConsumer membershipConsumer;
        if(response.getResponseCode()== ResponseCode.RESPONSE_ERROR||response.getData()==null){
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("confirm integral failed");
            return result;
        }else{
            membershipConsumer = (MembershipConsumer) response.getData();
        }

        response = integralDao.updateConfirm(addId);
        if(response.getResponseCode()== ResponseCode.RESPONSE_ERROR){
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("confirm integral failed");
            return result;
        }

        Map<String,Object> condition = new HashMap<>();
        condition.put("secondIntegral",integral+membershipConsumer.getSecondIntegral());
        response = membershipDao.update(condition);
        if(response.getResponseCode()== ResponseCode.RESPONSE_ERROR){
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("confirm integral failed");
            return result;
        }
        return result;
    }
}
