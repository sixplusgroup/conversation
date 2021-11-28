package finley.gmair.service;


import finley.gmair.model.installation.Userassign;
import finley.gmair.util.ResultData;

import java.util.Map;

public interface UserassignService {
    ResultData insert(Userassign userassign);

    ResultData fetch(Map<String, Object> condition);

    ResultData fetch(Map<String, Object> condition, int start, int length);

    ResultData principal(Map<String, Object> condition);

    ResultData principal(Map<String, Object> condition,int start,int length);

    ResultData confirmReservation(String userassignId);

    ResultData closeReservation(String userassignId);

    ResultData adjust(Userassign userassign);
}
