package finley.gmair.service;

import finley.gmair.model.machine.ModelVolume;
import finley.gmair.util.ResultData;

import java.util.Map;

public interface ModelVolumeService {
    ResultData create(ModelVolume modelVolume);

    ResultData fetch(Map<String, Object> condition);

    ResultData updateByModelId(Map<String, Object> condition);

    ResultData isNeedTurboVolume(String qrcode);

    ResultData fetchTurboVolume(String modelId);
}
