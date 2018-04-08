package finley.gmair.service;

import finley.gmair.model.admin.Admin;
import finley.gmair.util.ResultData;

import java.util.Map;

public interface AdminService {
    /**
     * method to put all filters in condition and fetch the result
     *
     * @param condition
     * @return
     */
    ResultData fetchAdmin(Map<String, Object> condition);

    /**
     * method to create an administrator account, used by backend system
     *
     * @param admin
     * @return
     */
    ResultData createAdmin(Admin admin);

    /**
     * method to revise the information for an existing administrator account
     *
     * @param admin
     * @return
     */
    ResultData reviseAdmin(Admin admin);
}
