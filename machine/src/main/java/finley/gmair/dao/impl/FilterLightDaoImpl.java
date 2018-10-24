package finley.gmair.dao.impl;

import finley.gmair.dao.BaseDao;
import finley.gmair.dao.FilterLightDao;
import finley.gmair.model.machine.FilterLight;
import finley.gmair.util.ResponseCode;
import finley.gmair.util.ResultData;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public class FilterLightDaoImpl extends BaseDao implements FilterLightDao {

    @Override
    public ResultData insert(FilterLight filterLight){
        ResultData result = new ResultData();
        try{
            sqlSession.insert("gmair.machine.filterLight.insert",filterLight);
            result.setData(filterLight);
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
            List<FilterLight> list = sqlSession.selectList("gmair.machine.filterLight.query",condition);
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
    public ResultData update(Map<String, Object> condition){
        ResultData result = new ResultData();
        try{
            sqlSession.update("gmair.machine.filterLight.update",condition);
        }catch (Exception e){
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription(e.getMessage());
        }
        return result;
    }


}
