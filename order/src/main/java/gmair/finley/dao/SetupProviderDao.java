package gmair.finley.dao;



import finley.gmair.model.order.SetupProvider;
import finley.gmair.util.ResultData;

import java.util.Map;

public interface SetupProviderDao {
    ResultData insert(SetupProvider provider);

    ResultData query(Map<String, Object> condition);

    ResultData update(SetupProvider provider);

    ResultData delete(String providerId);
}
