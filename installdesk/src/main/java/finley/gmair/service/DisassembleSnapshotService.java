package finley.gmair.service;

import finley.gmair.model.installation.SnapshotDisassemble;
import finley.gmair.util.ResultData;

import java.util.Map;

public interface DisassembleSnapshotService {
    ResultData create(SnapshotDisassemble snapshot);

    ResultData fetch(Map<String, Object> condition);

    ResultData block(String snapshotId);

    ResultData remove(String snapshotId);
}
