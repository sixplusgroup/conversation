package finley.gmair.service.impl;

import finley.gmair.dao.AdminRoleDao;
import finley.gmair.service.AdminRoleService;
import finley.gmair.util.ResultData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;


@Service
public class AdminRoleServiceImpl implements AdminRoleService{

    @Autowired
    AdminRoleDao adminRoleDao;

    @Override
    public ResultData fetchAdminRole(Map<String, Object> condition) {
        return adminRoleDao.queryAdminRole(condition);
    }
}
