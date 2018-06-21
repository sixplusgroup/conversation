package finley.gmair.service;

import finley.gmair.model.machine.BoardVersion;
import finley.gmair.util.ResultData;

import java.util.Map;

public interface BoardVersionService {
    ResultData createBoardVersion(BoardVersion version);

    ResultData fetchBoardVersion(Map<String, Object> condition);
}
