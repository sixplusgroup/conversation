package finley.gmair.dao;

import finley.gmair.model.installation.Reconnaissance;
import finley.gmair.util.ResultData;

import java.util.Map;

public interface ReconnaissanceDao {
    ResultData insert(Reconnaissance reconnaissance);

    ResultData query(Map<String, Object> condition);

    ResultData update(Reconnaissance reconnaissance);
}
