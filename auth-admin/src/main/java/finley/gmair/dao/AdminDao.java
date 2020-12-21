package finley.gmair.dao;

import finley.gmair.form.admin.AdminPartInfoQuery;
import finley.gmair.model.admin.Admin;
import finley.gmair.util.ResultData;
import finley.gmair.vo.admin.AdminPartInfoVo;

import java.util.List;
import java.util.Map;

public interface AdminDao {
    ResultData insert(Admin admin);

    ResultData query(Map<String, Object> condition);

    List<AdminPartInfoVo> queryAdminAccounts(AdminPartInfoQuery query);

    ResultData update(Admin admin);
}
