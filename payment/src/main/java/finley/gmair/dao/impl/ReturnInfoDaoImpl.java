package finley.gmair.dao.impl;

import finley.gmair.dao.BaseDao;
import finley.gmair.dao.ReturnInfoDao;
import finley.gmair.model.payment.ReturnInfo;
import finley.gmair.util.ResponseCode;
import finley.gmair.util.ResultData;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public class ReturnInfoDaoImpl extends BaseDao implements ReturnInfoDao {

    public ResultData insert(ReturnInfo returnInfo) {
        ResultData result = new ResultData();
        try {
            sqlSession.insert("gmair.payment.returnInfo.insert", returnInfo);
            result.setData(returnInfo);
        } catch (Exception e) {
            e.printStackTrace();
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription(e.getMessage());
        }
        return result;
    }

    public ResultData query(Map<String, Object> condition) {
        ResultData result = new ResultData();
        try{
            List<ReturnInfo> list = sqlSession.selectList("gmair.payment.returnInfo.query", condition);
            if (list.isEmpty()) {
                result.setResponseCode(ResponseCode.RESPONSE_NULL);
            }
            result.setData(list);
        }
        catch (Exception e) {
            e.printStackTrace();
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription(e.getMessage());
        }
        return result;
    }

}
