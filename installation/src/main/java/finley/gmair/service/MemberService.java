package finley.gmair.service;

import finley.gmair.model.installation.Member;
import finley.gmair.util.ResultData;

import java.util.Map;

public interface MemberService {
    ResultData createMember(Member member);

    ResultData fetchMember(Map<String, Object> condition);

    ResultData updateMember(Member member);

}
