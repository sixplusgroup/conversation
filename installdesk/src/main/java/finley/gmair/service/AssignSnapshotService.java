package finley.gmair.service;

import finley.gmair.model.installation.Snapshot;
import finley.gmair.util.ResultData;

import java.util.Map;

public interface AssignSnapshotService {
    ResultData create(Snapshot snapshot);

    ResultData fetch(Map<String, Object> condition);

    ResultData block(String snapshotId);

    ResultData remove(String snapshotId);
}
