package finley.gmair.service;

import finley.gmair.model.openplatform.CorpProfile;
import finley.gmair.util.ResultData;

import java.util.Map;

public interface CorpProfileService {
    ResultData create(CorpProfile corpProfile);

    ResultData fetch(Map<String, Object> condition);
}
