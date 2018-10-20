package finley.gmair.service;

import finley.gmair.model.installation.Snapshot;
import finley.gmair.util.ResultData;

import java.util.Map;

public interface SnapshotService {

    ResultData fetchSnapshot(Map<String, Object> condition);

    ResultData createSnapshot(Snapshot snapshot);

    ResultData updateSnapshot(Snapshot snapshot);

    ResultData deleteSnapshot(String snapshotId);
}
