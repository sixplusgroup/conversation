package finley.gmair.dao;

import finley.gmair.model.installation.Snapshot;
import finley.gmair.util.ResultData;

import java.util.Map;

public interface SnapshotDao {

    ResultData insertSnapshot(Snapshot snapshot);

    ResultData querySnapshot(Map<String, Object> condition);

}
