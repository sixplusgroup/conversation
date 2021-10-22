package finley.gmair.dao.impl;

import finley.gmair.dao.BaseDao;
import finley.gmair.dao.BoundaryPM2_5Dao;
import finley.gmair.model.machine.BoundaryPM2_5;
import finley.gmair.util.IDGenerator;
import finley.gmair.util.ResponseCode;
import finley.gmair.util.ResultData;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public class BoundaryPM2_5DaoImpl extends BaseDao implements BoundaryPM2_5Dao {

    @Override
    public ResultData insert(BoundaryPM2_5 boundaryPM2_5){
        ResultData result = new ResultData();
        boundaryPM2_5.setBoundaryId(IDGenerator.generate("PBD"));
        try{
            sqlSession.insert("gmair.machine.pm2_5_boundary.insert",boundaryPM2_5);
            result.setData(boundaryPM2_5);
        }catch (Exception e){
            e.printStackTrace();
            result.setDescription(e.getMessage());
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
        }
        return result;
    }

    @Override
    public ResultData query(Map<String, Object> condition){
        ResultData result = new ResultData();
        try{
            List<BoundaryPM2_5> list = sqlSession.selectList("gmair.machine.pm2_5_boundary.query",condition);
            if (list.isEmpty()) {
                result.setResponseCode(ResponseCode.RESPONSE_NULL);
            }
            result.setData(list);
        }catch (Exception e){
            e.printStackTrace();
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription(e.getMessage());
        }
        return result;
    }

    @Override
    public ResultData updateByModelId(Map<String, Object> condition){
        ResultData result = new ResultData();
        try{
            sqlSession.update("gmair.machine.pm2_5_boundary.updateByModelId",condition);
        }catch (Exception e){
            e.printStackTrace();
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription(e.getMessage());
        }
        return result;
    }
}
