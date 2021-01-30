package finley.gmair.dao;

import java.util.List;

public interface ModelFilterDao {

    List<String> selectFilerIdByModelId(String modelId);
}