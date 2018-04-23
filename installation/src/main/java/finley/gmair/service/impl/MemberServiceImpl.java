package finley.gmair.service.impl;

import finley.gmair.dao.MemberDao;
import finley.gmair.model.installation.Member;
import finley.gmair.service.MemberService;
import finley.gmair.util.ResponseCode;
import finley.gmair.util.ResultData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class MemberServiceImpl implements MemberService {
    @Autowired
    private MemberDao memberDao;

    @Override
    public ResultData fetchMember(Map<String, Object> condition){
        ResultData result = new ResultData();
        ResultData response = memberDao.queryMember(condition);
        if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
            result.setResponseCode(ResponseCode.RESPONSE_OK);
            result.setData(response.getData());
            result.setDescription("Success to fetch member");
        }
        if (response.getResponseCode() == ResponseCode.RESPONSE_NULL) {
            result.setResponseCode(ResponseCode.RESPONSE_NULL);
            result.setDescription("No member found");
        }
        if (response.getResponseCode() == ResponseCode.RESPONSE_ERROR) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("Fail to fetch member");
        }
        return result;
    }

    @Override
    public ResultData createMember(Member member){
        ResultData result = new ResultData();

        Map<String, Object> condition = new HashMap<>();
        condition.put("memberPhone",member.getMemberPhone());
        condition.put("blockFalg",false);

        ResultData response = memberDao.queryMember(condition);
        if(response.getResponseCode() == ResponseCode.RESPONSE_OK)
        {
            result.setResponseCode(ResponseCode.RESPONSE_OK);
            result.setDescription(new StringBuffer("member with phone: ").append(member.getMemberPhone()).append(" already exist").toString());
            return result;
        }
        if (result.getResponseCode() == ResponseCode.RESPONSE_ERROR) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("Fail to finish the prerequisite check");
            return result;
        }

        response=memberDao.insertMember(member);
        if(response.getResponseCode() == ResponseCode.RESPONSE_OK){
            result.setResponseCode(ResponseCode.RESPONSE_OK);
            result.setData(response.getData());
        }
        else if(response.getResponseCode() == ResponseCode.RESPONSE_ERROR){
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("Fail to insert member" + member.toString());
        }
        return result;
    }


}
