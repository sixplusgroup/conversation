package finley.gmair.dao.impl;

import finley.gmair.dao.BaseDao;
import finley.gmair.dao.ExpressDao;
import finley.gmair.model.express.Express;
import finley.gmair.util.ResponseCode;
import finley.gmair.util.ResultData;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Repository
public class ExpressDaoImpl extends BaseDao implements ExpressDao {

    private final static String Collection_Express = "express_record";

    @Override
    public ResultData query(Map<String, Object> condition) {
        ResultData result = new ResultData();
        List<Express> list = new ArrayList<>();
        try {
            if (condition.containsKey("expressNo") && condition.containsKey("company")) {
                list = sqlSession.selectList("gmair.install.express.query", condition);
            }
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
    public ResultData insert(Express express) {
        ResultData result = new ResultData();
        try {
            sqlSession.insert("gmair.install.express.insert", express);
            result.setData(express);
        } catch (Exception e) {
            e.printStackTrace();
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription(e.getMessage());
        }
        return result;
    }

    @Override
    public ResultData update(Express express) {
        ResultData result = new ResultData();
        try {
            sqlSession.update("gmair.install.express.update", express);
        } catch (Exception e) {
            e.printStackTrace();
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription(e.getMessage());
        }
        return result;
    }
}
