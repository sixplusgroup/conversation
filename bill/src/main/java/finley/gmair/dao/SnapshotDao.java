package finley.gmair.dao;

import finley.gmair.model.bill.DealSnapshot;
import finley.gmair.util.ResultData;

import java.util.Map;

public interface SnapshotDao {

    ResultData query(Map<String, Object> condition);

    ResultData insert(DealSnapshot snapshot);

    ResultData delete(String snapshotId);
}
