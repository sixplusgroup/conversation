package finley.gmair.service;

import finley.gmair.model.formaldehyde.CaseProfile;
import finley.gmair.util.ResultData;

import java.util.Map;

public interface CaseProfileService {
    ResultData create(CaseProfile caseProfile);

    ResultData fetch(Map<String, Object> condition);

    ResultData update(Map<String, Object> condition);
}
