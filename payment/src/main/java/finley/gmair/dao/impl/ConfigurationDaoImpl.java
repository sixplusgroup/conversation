package finley.gmair.dao.impl;

import finley.gmair.dao.BaseDao;
import finley.gmair.dao.ConfigurationDao;
import finley.gmair.model.payment.Configuration;
import finley.gmair.util.ResponseCode;
import finley.gmair.util.ResultData;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public class ConfigurationDaoImpl extends BaseDao implements ConfigurationDao {

    @Override
    public ResultData query() {
        ResultData result = new ResultData();
        try{
            List<Configuration> list = sqlSession.selectList("gmair.payment.configuration.query");
            if (list.isEmpty()) {
                result.setResponseCode(ResponseCode.RESPONSE_NULL);
            } else {
                result.setResponseCode(ResponseCode.RESPONSE_OK);
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
