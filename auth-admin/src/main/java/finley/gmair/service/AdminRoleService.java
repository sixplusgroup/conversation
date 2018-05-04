package finley.gmair.service;

import finley.gmair.util.ResultData;

import java.util.Map;

public interface AdminRoleService {


    /**
     * method to fetch role about admin user
     * @param condition: java.util.Map
     * @return: ResultData
     */
    ResultData fetchAdminRole(Map<String, Object> condition);

}
