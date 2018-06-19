package finley.gmair.dao.impl;

import finley.gmair.dao.BaseDao;
import finley.gmair.dao.ExpressParcelDao;
import finley.gmair.model.express.ExpressOrder;
import finley.gmair.model.express.ExpressParcel;
import finley.gmair.util.IDGenerator;
import finley.gmair.util.ResponseCode;
import finley.gmair.util.ResultData;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public class ExpressParcelDaoImpl extends BaseDao implements ExpressParcelDao {

    @Override
    public ResultData queryExpressParcel(Map<String, Object> condition) {
        ResultData result = new ResultData();
        try {
            List<ExpressOrder> list = sqlSession.selectList("gmair.express.parcel.query", condition);
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
    public ResultData insertExpressParcel(ExpressParcel expressParcel) {
        ResultData result = new ResultData();
        expressParcel.setExpressId(IDGenerator.generate("EXP"));
        try {
            sqlSession.insert("gmair.express.parcel.insert", expressParcel);
            result.setData(expressParcel);
        } catch (Exception e) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription(e.getMessage());
        }
        return result;
    }


    public ResultData updateSingle(Map<String, Object> condition) {
        ResultData result = new ResultData();
        try {
            sqlSession.update("gmair.express.parcel.update", condition);
        } catch (Exception e) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription(e.getMessage());
            System.out.println(e.getMessage());
        }
        return result;
    }

    @Override
    public ResultData updateExpressParcel(Map<String, Object> condition) {
        ResultData result = new ResultData();
        try {
            sqlSession.update("gmair.express.parcel.updateBatch", condition);
        } catch (Exception e) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription(e.getMessage());
        }
        return result;
    }
}
