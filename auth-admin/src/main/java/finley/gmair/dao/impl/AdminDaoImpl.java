package finley.gmair.dao.impl;

import finley.gmair.dao.AdminDao;
import finley.gmair.dao.BaseDao;
import finley.gmair.form.admin.AdminPartInfoQuery;
import finley.gmair.model.admin.Admin;
import finley.gmair.util.IDGenerator;
import finley.gmair.util.ResponseCode;
import finley.gmair.util.ResultData;
import finley.gmair.vo.admin.AdminPartInfoVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Repository
public class AdminDaoImpl extends BaseDao implements AdminDao {

    private Logger logger = LoggerFactory.getLogger(AdminDaoImpl.class);

    @Override
    @Transactional
    public ResultData insert(Admin admin) {
        ResultData result = new ResultData();
        admin.setAdminId(IDGenerator.generate("ADN"));
        try {
            sqlSession.insert("gmair.admin.insert", admin);
            result.setData(admin);
        } catch (Exception e) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription(e.getMessage());
        }
        return result;
    }

    @Override
    public ResultData query(Map<String, Object> condition) {
        ResultData result = new ResultData();
        try {
            List<Admin> list = sqlSession.selectList("gmair.admin.query", condition);
            if (list.isEmpty()) {
                result.setResponseCode(ResponseCode.RESPONSE_NULL);
            }
            result.setData(list);
        } catch (Exception e) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription(e.getMessage());
        }
        return result;
    }

    @Override
    public List<AdminPartInfoVo> queryAdminAccounts(AdminPartInfoQuery query) {
        try {
            return sqlSession.selectList("gmair.admin.query_admin_accounts", query);
        } catch (Exception e) {
            logger.error("query admin accounts failed: " + e.getMessage());
        }
        return new ArrayList<>();
    }

    @Override
    public long queryAdminAccountsSize(AdminPartInfoQuery query) {
        try {
            return sqlSession.selectOne("gmair.admin.query_admin_accounts_size", query);
        } catch (Exception e) {
            logger.error("query admin accounts size failed: " + e.getMessage());
        }
        return 0;
    }

    @Override
    public ResultData update(Admin admin) {
        ResultData result = new ResultData();
        try {
            sqlSession.update("gmair.admin.update", admin);
            result.setData(admin);
        } catch (Exception e) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription(e.getMessage());
        }
        return result;
    }
}
