package finley.gmair.dao.impl;

import finley.gmair.dao.BaseDao;
import finley.gmair.dao.IndoorPm25HourlyDao;
import finley.gmair.model.analysis.IndoorPm25Hourly;
import finley.gmair.util.IDGenerator;
import finley.gmair.util.ResponseCode;
import finley.gmair.util.ResultData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public class IndoorPm25HourlyDaoImpl extends BaseDao implements IndoorPm25HourlyDao {

    private Logger logger = LoggerFactory.getLogger(IndoorPm25HourlyDaoImpl.class);

    @Override
    public ResultData query(Map<String, Object> condition){
        ResultData result = new ResultData();
        try{
            List<IndoorPm25Hourly> list = sqlSession.selectList("gmair.machine.indoor_pm25_hourly.query",condition);
            if (list.isEmpty()) {
                result.setResponseCode(ResponseCode.RESPONSE_NULL);
            }
            result.setData(list);
        }catch (Exception e){
            logger.info(e.getMessage());
            e.printStackTrace();
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription(e.getMessage());
        }
        return result;
    }

    @Override
    public ResultData insertBatch(List<IndoorPm25Hourly> list) {
        ResultData result = new ResultData();
        for (IndoorPm25Hourly mpd: list)
            if (mpd.getStatusId() == null)
                mpd.setStatusId(IDGenerator.generate("IDR"));
        try {
            sqlSession.insert("gmair.machine.indoor_pm25_hourly.insertBatch", list);
            result.setData(list);
        } catch (Exception e) {
            logger.info(e.getMessage());
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription(e.getMessage());
            e.printStackTrace();
        }
        return result;
    }

}
