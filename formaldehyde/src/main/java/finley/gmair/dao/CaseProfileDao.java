package finley.gmair.dao;

import finley.gmair.model.formaldehyde.CaseProfile;
import finley.gmair.util.ResultData;

import java.util.Map;

public interface CaseProfileDao {
    ResultData insert(CaseProfile caseProfile);

    ResultData query(Map<String, Object> condition);

    ResultData update(Map<String, Object> condition);
}
