package finley.gmair.dao;

import finley.gmair.model.admin.Admin;
import finley.gmair.util.ResultData;

import java.util.Map;

public interface AdminDao {
    ResultData insert(Admin admin);

    ResultData query(Map<String, Object> condition);

    ResultData update(Admin admin);
}
