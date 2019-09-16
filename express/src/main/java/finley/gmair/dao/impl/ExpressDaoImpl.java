package finley.gmair.dao.impl;

import com.mongodb.WriteResult;
import finley.gmair.dao.BaseDao;
import finley.gmair.dao.ExpressDao;
import finley.gmair.model.express.Express;
import finley.gmair.util.ResponseCode;
import finley.gmair.util.ResultData;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
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
            if (condition.containsKey("expressNo")) {
                list = mongoTemplate.find(new Query(Criteria.where("expressNo").is(condition.get("expressNo"))), Express.class, Collection_Express);
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
            mongoTemplate.insert(express, Collection_Express);
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
            Query query = new Query(Criteria.where("expressNo").is(express));
            //WriteResult wr = mongoTemplate.updateFirst();
        } catch (Exception e) {
            e.printStackTrace();
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription(e.getMessage());
        }
        return result;
    }
}
