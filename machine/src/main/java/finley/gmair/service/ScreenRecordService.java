package finley.gmair.service;

import finley.gmair.model.machine.v2.ScreenRecord;
import finley.gmair.util.ResultData;

import java.util.Map;

/**
 * @author shenghaohu@hshvip.com
 * @date 2018/6/19
 */
public interface ScreenRecordService {
    ResultData fetchScreenRecord(Map<String, Object> condition);

    ResultData createScreenRecord(ScreenRecord record);
}
