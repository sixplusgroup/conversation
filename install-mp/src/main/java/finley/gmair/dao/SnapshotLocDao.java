package finley.gmair.dao;

import finley.gmair.model.installation.SnapshotLoc;
import finley.gmair.util.ResultData;

import java.util.Map;

public interface SnapshotLocDao {
    ResultData insertSnapshotLoc(SnapshotLoc snapshotLoc);

    ResultData querySnapshotLoc(Map<String, Object> condition);
}
