package finley.gmair.dao.impl;

import finley.gmair.dao.AssignDao;
import finley.gmair.dao.BaseDao;
import finley.gmair.model.installation.Assign;
import finley.gmair.model.installation.AssignStatus;
import finley.gmair.util.IDGenerator;
import finley.gmair.util.ResponseCode;
import finley.gmair.util.ResultData;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Repository
public class AssignDaoImpl extends BaseDao implements AssignDao {

    @Override
    public ResultData insertAssign(Assign assign){
        ResultData result = new ResultData();
        assign.setAssignId(IDGenerator.generate("IAS"));
        try{
            sqlSession.insert("gmair.installation.assign.insert",assign);
            result.setData(assign);
        }
        catch(Exception e){
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription(e.getMessage());
        }
        return result;
    }

    @Override
    public ResultData queryAssign(Map<String, Object> condition) {
        ResultData result = new ResultData();
        List<Assign> list=new ArrayList<>();
        try{
            list=sqlSession.selectList("gmair.installation.assign.query",condition);
            result.setData(list);
        }
        catch(Exception e){
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription(e.getMessage());
        }

        if(result.getResponseCode()!=ResponseCode.RESPONSE_ERROR) {
            if (list.isEmpty() == true) {
                result.setResponseCode(ResponseCode.RESPONSE_NULL);
                result.setDescription("No assign found");
            } else {
                result.setResponseCode(ResponseCode.RESPONSE_OK);
                result.setDescription("Success to found assign");
            }
        }
        return result;
    }

    @Override
    public ResultData updateAssign(Assign assign){
        ResultData result = new ResultData();
        try{
            sqlSession.update("gmair.installation.assign.update",assign);
            result.setData(assign);
        }
        catch (Exception e){
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription(e.getMessage());
        }
        return result;
    }
}
