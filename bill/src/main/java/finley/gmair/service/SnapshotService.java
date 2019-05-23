package finley.gmair.service;

import finley.gmair.model.bill.DealSnapshot;
import finley.gmair.util.ResultData;

import java.util.Map;

public interface SnapshotService {

    ResultData fetchSnapshot(Map<String, Object> condition);

    ResultData createSnapshot(DealSnapshot snapshot);

    ResultData deleteSnapshot(String snapshotId);
}
