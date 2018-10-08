package finley.gmair.dao;

import finley.gmair.model.machine.BoardVersion;
import finley.gmair.util.ResultData;

import java.util.Map;

public interface BoardVersionDao {
    ResultData insertBoardVersion(BoardVersion version);

    ResultData queryBoardVersion(Map<String, Object> condition);

    ResultData delete(String machineId);
}
