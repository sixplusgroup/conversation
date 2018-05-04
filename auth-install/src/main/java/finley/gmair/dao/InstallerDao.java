package finley.gmair.dao;

import finley.gmair.model.installation.Member;
import finley.gmair.util.ResultData;

import java.util.Map;

public interface InstallerDao {
    ResultData query(Map<String, Object> condition);

    ResultData update(Member member);
}
