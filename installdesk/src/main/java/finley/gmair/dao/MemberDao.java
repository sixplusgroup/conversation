package finley.gmair.dao;

import finley.gmair.model.installation.Member;
import finley.gmair.util.ResultData;

import java.util.Map;

public interface MemberDao {
    ResultData insert(Member member);

    ResultData query(Map<String, Object> condition);

    ResultData query(Map<String, Object> condition, int start, int length);

    ResultData update(Map<String, Object> condition);

    ResultData block(String memberId);

    ResultData remove(String memberId);
}
