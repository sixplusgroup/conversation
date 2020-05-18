package finley.gmair.service;

import finley.gmair.model.installation.SnapshotSurvey;
import finley.gmair.util.ResultData;

import java.util.Map;

public interface SurveySnapshotService {
    ResultData create(SnapshotSurvey snapshot);

    ResultData fetch(Map<String, Object> condition);

    ResultData block(String snapshotId);

    ResultData remove(String snapshotId);
}
