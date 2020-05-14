package finley.gmair.service;

import finley.gmair.model.installation.SnapshotFix;
import finley.gmair.util.ResultData;

import java.util.Map;

public interface FixSnapshotService {
    ResultData create(SnapshotFix snapshot);

    ResultData fetch(Map<String, Object> condition);

    ResultData block(String snapshotId);

    ResultData remove(String snapshotId);
}
