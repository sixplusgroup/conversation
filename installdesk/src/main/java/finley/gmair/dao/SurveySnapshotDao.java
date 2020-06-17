package finley.gmair.dao;

import finley.gmair.model.installation.SnapshotSurvey;
import finley.gmair.util.ResultData;

import java.util.Map;

public interface SurveySnapshotDao {
    ResultData insert(SnapshotSurvey snapshot);

    ResultData query(Map<String, Object> condition);

    ResultData block(String snapshotId);

    ResultData remove(String snapshotId);
}
