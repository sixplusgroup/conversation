package finley.gmair.dao.impl;

import finley.gmair.dao.BaseDao;
import finley.gmair.dao.ExpressDao;
import finley.gmair.model.drift.Express;
import finley.gmair.util.IDGenerator;
import finley.gmair.util.ResponseCode;
import finley.gmair.util.ResultData;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public class ExpressDaoImpl extends BaseDao implements ExpressDao {

    @Override
    public ResultData queryExpress(Map<String, Object> condition){
        ResultData result = new ResultData();
        try {
            List<Express> list = sqlSession.selectList("gmair.drift.express.query", condition);
            if (list.isEmpty()) {
                result.setResponseCode(ResponseCode.RESPONSE_NULL);
            }
            result.setData(list);
        } catch (Exception e) {
            e.printStackTrace();
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription(e.getMessage());
        }
        return result;
    }

    @Override
    public ResultData insertExpress(Express express){
        ResultData result = new ResultData();
        express.setExpressId(IDGenerator.generate("ACT"));
        try {
            sqlSession.insert("gmair.drift.express.insert", express);
            result.setData(express);
        } catch (Exception e) {
            e.printStackTrace();
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription(e.getMessage());
        }
        return result;
    }
}
