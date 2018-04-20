package finley.gmair.dao.impl;

import finley.gmair.dao.BaseDao;
import finley.gmair.dao.MemberDao;
import finley.gmair.model.installation.Member;
import finley.gmair.util.IDGenerator;
import finley.gmair.util.ResponseCode;
import finley.gmair.util.ResultData;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public class MemberDaoImpl extends BaseDao implements MemberDao {
    @Override
    public ResultData insertMember(Member member) {
        ResultData result =new ResultData();
        member.setMemberId(IDGenerator.generate("ISM"));
        try{
            sqlSession.insert("gmair.installation.member.insert",member);
            result.setData(member);
        }
        catch (Exception e){
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription(e.getMessage());
        }
        return result;
    }

    @Override
    public ResultData queryMember(Map<String, Object> condition) {
        ResultData result =new ResultData();
        try{
            List<Member> list = sqlSession.selectList("gmair.installation.member.query", condition);
            if (list.isEmpty()) {
                result.setResponseCode(ResponseCode.RESPONSE_NULL);
            }
            result.setData(list);
        } catch (Exception e) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription(e.getMessage());
        }
        return result;
    }
    @Override
    public ResultData updateMember(Member member) {
        ResultData result =new ResultData();
        try {
            sqlSession.update("gmair.installation.member.update", member);
            result.setData(member);
        } catch (Exception e) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription(e.getMessage());
        }
        return result;
    }
}
