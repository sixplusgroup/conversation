package finley.gmair.dao;

import finley.gmair.model.assemble.Snapshot;
import finley.gmair.util.ResultData;

import java.util.Map;

public interface SnapshotDao {
    ResultData insert(Snapshot snapshot);

    ResultData query(Map<String, Object> condition);

}
