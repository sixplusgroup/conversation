package finley.gmair.service;

import finley.gmair.model.drift.Activity;
import finley.gmair.util.ResultData;

import java.util.Map;

public interface ActivityService {
    ResultData fetchActivity(Map<String, Object> condition);

    ResultData createActivity(Activity activity);

    ResultData modifyActivity(Map<String, Object> condition);

    ResultData fetchActivityEquipment(Map<String, Object> condition);

    ResultData fetchActivityThumbnail(Map<String, Object> condition);
}
