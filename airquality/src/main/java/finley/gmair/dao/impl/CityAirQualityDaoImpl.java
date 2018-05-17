package finley.gmair.dao.impl;

import finley.gmair.dao.CityAirQualityDao;
import finley.gmair.dao.BaseDao;
import finley.gmair.model.air.CityAirQuality;
import finley.gmair.util.ResponseCode;
import finley.gmair.util.ResultData;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public class CityAirQualityDaoImpl extends BaseDao implements CityAirQualityDao {

    @Override
    public ResultData insert(CityAirQuality airQuality) {
        ResultData result = new ResultData();
        try {
            sqlSession.insert("gmair.airquality.cityairquality.insert", airQuality);
            result.setData(airQuality);
        } catch (Exception e) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription(e.getMessage());
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public ResultData insertLatest(CityAirQuality airQuality) {
        ResultData result = new ResultData();
        try {
            sqlSession.insert("gmair.airquality.cityairquality.insertLatest", airQuality);
            result.setData(airQuality);
        } catch (Exception e) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription(e.getMessage());
        }
        return result;
    }

    @Override
    public ResultData insertBatch(List<CityAirQuality> list) {
        ResultData result = new ResultData();
        try {
            sqlSession.insert("gmair.airquality.cityairquality.insertBatch", list);
            result.setData(list);
        } catch (Exception e) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription(e.getMessage());
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public ResultData insertLatestBatch(List<CityAirQuality> list) {
        ResultData result = new ResultData();
        try {
            sqlSession.insert("gmair.airquality.cityairquality.insertLatestBatch", list);
            result.setData(list);
        } catch (Exception e) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription(e.getMessage());
        }
        return result;
    }
}
