package finley.gmair.service;

import finley.gmair.model.installation.SnapshotChangeMachine;
import finley.gmair.util.ResultData;

import java.util.Map;

public interface ChangeMachineSnapshotService {
    ResultData create(SnapshotChangeMachine snapshot);

    ResultData fetch(Map<String, Object> condition);

    ResultData block(String snapshotId);

    ResultData remove(String snapshotId);
}
