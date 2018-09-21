package finley.gmair.service;

import finley.gmair.model.assemble.Snapshot;
import finley.gmair.util.ResultData;

import java.util.Map;

public interface SnapshotService {

    ResultData create(Snapshot snapshot);

    ResultData fetch(Map<String,Object> condition);
}
