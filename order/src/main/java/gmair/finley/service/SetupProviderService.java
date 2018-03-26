package gmair.finley.service;

import finley.gmair.model.order.SetupProvider;
import finley.gmair.util.ResultData;

import java.util.Map;

public interface SetupProviderService {

    ResultData fetchMissionChannel(Map<String, Object> condition);

    ResultData create(SetupProvider missionChannel);

    ResultData modifyMissionChannel(SetupProvider missionChannel);

    ResultData deleteMissionChannel(String channelId);
}

