package finley.gmair.dao.impl;

import finley.gmair.dao.AirQualityDao;
import finley.gmair.dao.BaseDao;
import finley.gmair.model.air.AirQuality;
import finley.gmair.util.ResponseCode;
import finley.gmair.util.ResultData;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public class AirQualityDaoImpl extends BaseDao implements AirQualityDao{

    @Override
    public ResultData insert(AirQuality airQuality) {
        ResultData result = new ResultData();
        try {
            sqlSession.insert("gmair.airquality.airquality.insert", airQuality);
            result.setData(airQuality);
        } catch (Exception e) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription(e.getMessage());
        }
        return result;
    }

    @Override
    public ResultData insertLatest(AirQuality airQuality) {
        ResultData result = new ResultData();
        try {
            sqlSession.insert("gmair.airquality.airquality.insertLatest", airQuality);
            result.setData(airQuality);
        } catch (Exception e) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription(e.getMessage());
        }
        return result;
    }

    @Override
    public ResultData insertBatch(List<AirQuality> list) {
        ResultData result = new ResultData();
        try {
            sqlSession.insert("gmair.airquality.airquality.insertBatch", list);
            result.setData(list);
        } catch (Exception e) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription(e.getMessage());
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public ResultData insertLatestBatch(List<AirQuality> list) {
        ResultData result = new ResultData();
        try {
            sqlSession.insert("gmair.airquality.airquality.insertLatestBatch", list);
            result.setData(list);
        } catch (Exception e) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription(e.getMessage());
        }
        return result;
    }
}
