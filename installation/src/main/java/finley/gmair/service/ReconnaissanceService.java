package finley.gmair.service;

import finley.gmair.model.installation.Reconnaissance;
import finley.gmair.util.ResultData;

import java.util.Map;

public interface ReconnaissanceService {
    ResultData createReconnaissance(Reconnaissance reconnaissance);

    ResultData fetchReconnaissance(Map<String, Object> condition);
}
