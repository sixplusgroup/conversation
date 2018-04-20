package finley.gmair.dao;

import finley.gmair.model.installation.Member;
import finley.gmair.util.ResultData;

import java.util.Map;

public interface MemberDao {
    ResultData insertMember(Member member);

    ResultData queryMember(Map<String, Object> condition);

    ResultData updateMember(Member member);
}
