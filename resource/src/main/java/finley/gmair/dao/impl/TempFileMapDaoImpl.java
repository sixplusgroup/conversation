package finley.gmair.dao.impl;

import finley.gmair.dao.BaseDao;
import finley.gmair.dao.TempFileMapDao;
import finley.gmair.model.resource.FileMap;
import finley.gmair.util.IDGenerator;
import finley.gmair.util.ResponseCode;
import finley.gmair.util.ResultData;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Repository
public class TempFileMapDaoImpl extends BaseDao implements TempFileMapDao {

    @Override
    public ResultData insertTempFileMap(FileMap tempFileMap){
        ResultData result = new ResultData();
        tempFileMap.setFileId(IDGenerator.generate("RTF"));
        try{

            sqlSession.insert("gmair.resource.tempfilemap.insert", tempFileMap);
            result.setData(tempFileMap);
        }
        catch (Exception e){
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription(e.getMessage());
        }
        return result;
    }

    @Override
    public ResultData queryTempFileMap(Map<String, Object> condition){
        ResultData result = new ResultData();
        List<FileMap> list = new ArrayList<>();
        try{
            list = sqlSession.selectList("gmair.resource.tempfilemap.query", condition);
            result.setData(list);

        } catch (Exception e) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription(e.getMessage());
        }

        if(result.getResponseCode()!=ResponseCode.RESPONSE_ERROR) {
            if (list.isEmpty() == true) {
                result.setResponseCode(ResponseCode.RESPONSE_NULL);
                result.setDescription("No tempfilemap found");
            } else {
                result.setResponseCode(ResponseCode.RESPONSE_OK);
                result.setDescription("Success to found tempfilemap");
            }
        }
        return result;
    }
}
