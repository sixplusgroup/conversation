package finley.gmair.dao;

import finley.gmair.model.installation.SnapshotChangeMachine;
import finley.gmair.util.ResultData;

import java.util.Map;

public interface ChangeMachineSnapshotDao {
    ResultData insert(SnapshotChangeMachine snapshot);

    ResultData query(Map<String, Object> condition);

    ResultData block(String snapshotId);

    ResultData remove(String snapshotId);
}
