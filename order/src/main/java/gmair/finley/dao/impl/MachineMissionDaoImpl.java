package gmair.finley.dao.impl;


import finley.gmair.model.order.MachineMission;
import finley.gmair.util.IDGenerator;
import finley.gmair.util.ResponseCode;
import finley.gmair.util.ResultData;
import finley.gmair.vo.order.MachineMissionVo;
import gmair.finley.dao.BaseDao;
import gmair.finley.dao.MachineMissionDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;


import java.util.List;
import java.util.Map;

/**
 * Created by hushe on 2018/1/17.
 */
@Repository
public class MachineMissionDaoImpl extends BaseDao implements MachineMissionDao {
    private Logger logger = LoggerFactory.getLogger(MachineMissionDaoImpl.class);
    private final Object lock = new Object();

    @Override
    public ResultData query(Map<String, Object> condition) {
        ResultData result = new ResultData();
        try {
            List<MachineMissionVo> list = sqlSession.selectList("management.order.machineMission.query", condition);
            if (list.isEmpty()) {
                result.setResponseCode(ResponseCode.RESPONSE_NULL);
            } else {
                result.setData(list);
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription(e.getMessage());
        }
        return result;
    }

    @Override
    public ResultData insert(MachineMission machineMission) {
        ResultData result = new ResultData();
        machineMission.setMissionId(IDGenerator.generate("MM"));
        synchronized (lock) {
            try {
                sqlSession.insert("management.order.machineMission.insert", machineMission);
                result.setData(machineMission);
            } catch (Exception e) {
                logger.error(e.getMessage());
                result.setResponseCode(ResponseCode.RESPONSE_ERROR);
                result.setDescription(e.getMessage());
            }
        }
        return result;
    }
}
