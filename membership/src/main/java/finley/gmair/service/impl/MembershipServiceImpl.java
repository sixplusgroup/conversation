package finley.gmair.service.impl;

import finley.gmair.dao.MembershipDao;
import finley.gmair.model.membership.MembershipConsumer;
import finley.gmair.service.MembershipService;
import finley.gmair.util.ResponseCode;
import finley.gmair.util.ResultData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * @ClassName MembershipServiceImpl
 * @Description TODO
 * @Author Joby
 * @Date 2021/7/16 17:30
 */
@Service
public class MembershipServiceImpl implements MembershipService {
    @Autowired
    private MembershipDao membershipDao;
    @Override
    public ResultData create(String consumerId) {
        MembershipConsumer member = new MembershipConsumer(consumerId);
        return membershipDao.insert(member);
    }

    @Override
    public boolean checkMemberIsValid(String consumerId) {
        ResultData response = membershipDao.getOneById(consumerId);
        if(response.getResponseCode()== ResponseCode.RESPONSE_ERROR||response.getData()==null){
            return false;
        }
        return true;
    }

    @Override
    public ResultData withdrawIntegral(String consumerId, Integer integral) {
        ResultData result = new ResultData();
        ResultData response =  membershipDao.getOneById(consumerId);
        if(response.getResponseCode()!=ResponseCode.RESPONSE_OK){
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription(response.getDescription());
            return result;
        }
        MembershipConsumer membershipConsumer = (MembershipConsumer)response.getData();
        if(membershipConsumer.getAllIntegral()<integral){
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("you don't have enough integral");
            return result;
        }
        if(membershipConsumer.getFirstIntegral()<integral){
            Map<String,Object> condition = new HashMap<>();
            condition.put("firstIntegral",0);
            condition.put("secondIntegral",membershipConsumer.getAllIntegral()-integral);
            response = membershipDao.update(condition);
        }else{
            Map<String,Object> condition = new HashMap<>();
            condition.put("firstIntegral",membershipConsumer.getFirstIntegral()-integral);
            response = membershipDao.update(condition);
        }
        if(response.getResponseCode()!=ResponseCode.RESPONSE_OK){
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("the operation of `withdrawIntegral` failed");
            return result;
        }
        return result;
    }

    @Override
    public ResultData setEventScheduler() {
        return membershipDao.setEventScheduler();
    }
}
