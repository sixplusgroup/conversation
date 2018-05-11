package finley.gmair.service;

import finley.gmair.model.installation.SnapshotLoc;
import finley.gmair.util.ResultData;

import java.util.Map;

public interface SnapshotLocService {

    ResultData createSnapshotLoc(SnapshotLoc snapshotLoc);

    ResultData fetchSnapshotLoc(Map<String, Object> condition);
}
