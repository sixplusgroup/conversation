package finley.gmair.dao.impl;

import finley.gmair.dao.AdminDao;
import finley.gmair.dao.BaseDao;
import finley.gmair.model.admin.Admin;
import finley.gmair.util.IDGenerator;
import finley.gmair.util.ResponseCode;
import finley.gmair.util.ResultData;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Repository
public class AdminDaoImpl extends BaseDao implements AdminDao {

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
