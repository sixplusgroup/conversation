package finley.gmair.dao.impl;

import finley.gmair.dao.BaseDao;
import finley.gmair.dao.ExpressCompanyDao;
import finley.gmair.model.express.ExpressCompany;
import finley.gmair.util.IDGenerator;
import finley.gmair.util.ResponseCode;
import finley.gmair.util.ResultData;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public class ExpressCompanyDaoImpl extends BaseDao implements ExpressCompanyDao {
    @Override
    public ResultData insertExpressCompany(ExpressCompany company) {
        ResultData result = new ResultData();
        company.setCompanyId(IDGenerator.generate("EXC"));
        try {
            sqlSession.insert("gmair.express.insert", company);
            result.setData(company);
        } catch (Exception e) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription(e.getMessage());
        }
        return result;
    }

    @Override
    public ResultData queryExpressCompany(Map<String, Object> condition) {
        ResultData result = new ResultData();
        try {
            List<ExpressCompany> list = sqlSession.selectList("gmair.express.query", condition);
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
}
