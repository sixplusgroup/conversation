package finley.gmair.service;

import finley.gmair.model.installation.Snapshot;
import finley.gmair.util.ResultData;

import java.util.Map;

public interface SnapshotService {

    ResultData createSnapshot(Snapshot snapshot);

    ResultData fetchSnapshot(Map<String, Object> condition);

}
