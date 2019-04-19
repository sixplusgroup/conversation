package finley.gmair.dao;

import finley.gmair.model.installation.Snapshot;
import finley.gmair.util.ResultData;

import java.util.Map;

/**
 * @ClassName: AssignSnapshotDao
 * @Description: TODO
 * @Author fan
 * @Date 2019/4/19 2:36 PM
 */
public interface AssignSnapshotDao {
    ResultData insert(Snapshot snapshot);

    ResultData query(Map<String, Object> condition);

    ResultData block(String snapshotId);

    ResultData remove(String snapshotId);
}
