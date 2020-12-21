package finley.gmair.service;

import finley.gmair.form.admin.AdminPartInfoQuery;
import finley.gmair.model.admin.Admin;
import finley.gmair.util.ResultData;
import finley.gmair.vo.admin.AdminPartInfoVo;

import java.util.List;
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
     * 根据query给出的条件查询符合条件的账户信息
     * @param query 查询条件对象
     * @return 查询结果
     */
    List<AdminPartInfoVo> fetchAdminAccounts(AdminPartInfoQuery query);

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
