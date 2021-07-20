package finley.gmair.dao.impl;

import finley.gmair.dao.CaseLngLatDao;
import finley.gmair.model.formaldehyde.CaseLngLat;
import finley.gmair.util.IDGenerator;
import finley.gmair.util.ResponseCode;
import finley.gmair.util.ResultData;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public class CaseLngLatDaoImpl extends BaseDao implements CaseLngLatDao {

    @Override
    public ResultData insert(CaseLngLat caseLngLat) {
        ResultData result = new ResultData();
        caseLngLat.setRecordId(IDGenerator.generate("REC"));
        try {
            sqlSession.insert("gmair.formaldehyde.case_lng_lat.insert", caseLngLat);
            result.setData(caseLngLat);
        } catch (Exception e) {
            e.printStackTrace();
            result.setDescription(e.getMessage());
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
        }
        return result;
    }

    @Override
    public ResultData query(Map<String, Object> condition) {
        ResultData result = new ResultData();
        try {
            List<CaseLngLat> list = sqlSession.selectList("gmair.formaldehyde.case_lng_lat.query", condition);
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

}
