package finley.gmair.dao;

import finley.gmair.model.openplatform.CorpProfile;
import finley.gmair.util.ResultData;

import java.util.Map;

public interface CorpProfileDao {

    ResultData insert(CorpProfile corpProfile);

    ResultData query(Map<String, Object> condition);
}
