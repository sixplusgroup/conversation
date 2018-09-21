package finley.gmair.service;

import finley.gmair.model.assemble.CheckRecord;
import finley.gmair.util.ResultData;

import java.util.Map;

public interface CheckRecordService {

    ResultData create(CheckRecord checkRecord);

    ResultData fetch(Map<String,Object> condition);
}
