package finley.gmair.dao;

import finley.gmair.model.installation.SnapshotFix;
import finley.gmair.util.ResultData;

import java.util.Map;

public interface FixSnapshotDao {
    ResultData insert(SnapshotFix snapshot);

    ResultData query(Map<String, Object> condition);

    ResultData block(String snapshotId);

    ResultData remove(String snapshotId);
}
