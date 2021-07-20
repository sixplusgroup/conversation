package finley.gmair.dao;

import finley.gmair.model.installation.SnapshotDisassemble;
import finley.gmair.util.ResultData;

import java.util.Map;

public interface DisassembleSnapshotDao {
    ResultData insert(SnapshotDisassemble snapshot);

    ResultData query(Map<String, Object> condition);

    ResultData block(String snapshotId);

    ResultData remove(String snapshotId);
}
