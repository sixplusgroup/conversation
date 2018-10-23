package finley.gmair.dao;

import finley.gmair.model.installation.Snapshot;
import finley.gmair.util.ResultData;

import java.util.Map;

public interface SnapshotDao {

    ResultData query(Map<String, Object> condition);

    ResultData insert(Snapshot snapshot);

    ResultData update(Snapshot snapshot);
}
