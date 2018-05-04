package finley.gmair.dao.impl;

import finley.gmair.dao.AdminRoleDao;
import finley.gmair.dao.BaseDao;
import finley.gmair.util.ResponseCode;
import finley.gmair.util.ResultData;
import finley.gmair.vo.role.RoleVo;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;


@Repository
public class AdminRoleDaoImpl extends BaseDao implements AdminRoleDao {

    @Override
    public ResultData queryAdminRole(Map<String, Object> condition) {
        ResultData result = new ResultData();
        try {
            List<RoleVo> roles = sqlSession.selectList("gmair.adminrole.query", condition);
            if (roles.isEmpty()) {
                result.setResponseCode(ResponseCode.RESPONSE_NULL);
            } else {
                result.setData(roles);
            }
        } catch (Exception e) {
            e.printStackTrace();
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription(e.getMessage());
        }
        return result;
    }
}
