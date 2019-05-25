package finley.gmair.dao.impl;

import finley.gmair.dao.BaseDao;
import finley.gmair.dao.CorpMachineSubsDao;
import finley.gmair.model.openplatform.MachineSubscription;
import finley.gmair.util.IDGenerator;
import finley.gmair.util.ResponseCode;
import finley.gmair.util.ResultData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public class CorpMachineSubsDaoImpl extends BaseDao implements CorpMachineSubsDao {

    private Logger logger = LoggerFactory.getLogger(CorpMachineSubsDaoImpl.class);

    @Override
    public ResultData insert(MachineSubscription machineSubscription) {
        ResultData result = new ResultData();
        machineSubscription.setSubscriptionId(IDGenerator.generate("SUB"));
        try {
            sqlSession.insert("gmair.openplatform.subscribe.insert", machineSubscription);
            result.setData(machineSubscription);
        } catch (Exception e) {
            logger.error(e.getMessage());
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription(e.getMessage());
        }
        return result;
    }

    @Override
    public ResultData query(Map<String, Object> condition) {
        ResultData result = new ResultData();
        try {
            List list = sqlSession.selectList("gmair.openplatform.subscribe.query", condition);
            if (list.isEmpty()) {
                result.setResponseCode(ResponseCode.RESPONSE_NULL);
            }
            result.setData(list);
        } catch (Exception e) {
            logger.error(e.getMessage());
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription(e.getMessage());
        }
        return result;
    }
}
