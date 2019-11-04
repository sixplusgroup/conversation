package finley.gmair.service;

import finley.gmair.model.installation.Member;
import finley.gmair.util.ResultData;

import java.util.Map;

public interface MemberService {
    ResultData create(Member member);

    ResultData fetch(Map<String, Object> condition);

    ResultData fetch(Map<String, Object> condition, int start, int length);

    ResultData update(Map<String, Object> condition);

    ResultData block(String memberId);

    ResultData remove(String memberId);

    ResultData watchTeam(String memberId, String teamId);

    ResultData fetchTeams(Map<String, Object> condition);

    ResultData fetchMemberTeam(Map<String, Object> condition);
}
