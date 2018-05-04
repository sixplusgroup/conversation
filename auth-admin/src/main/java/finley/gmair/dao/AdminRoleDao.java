package finley.gmair.dao;

import finley.gmair.util.ResultData;

import java.util.Map;

public interface AdminRoleDao {

    /**
     * method to fetch role about admin user
     * @param condition: java.util.Map
     * @return: ResultData
     */
    ResultData queryAdminRole(Map<String, Object> condition);
}
