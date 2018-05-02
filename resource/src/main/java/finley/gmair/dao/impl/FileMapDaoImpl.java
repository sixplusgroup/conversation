package finley.gmair.dao.impl;

import finley.gmair.dao.BaseDao;
import finley.gmair.dao.FileMapDao;
import finley.gmair.model.resource.FileMap;
import finley.gmair.util.IDGenerator;
import finley.gmair.util.ResponseCode;
import finley.gmair.util.ResultData;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Repository
public class FileMapDaoImpl extends BaseDao implements FileMapDao {


    @Override
    public ResultData insertFileMap(FileMap fileMap){
        ResultData result = new ResultData();
        fileMap.setFileId(IDGenerator.generate("RFM"));
        try{

            sqlSession.insert("gmair.resource.filemap.insert", fileMap);
            result.setData(fileMap);
        }
        catch (Exception e){
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription(e.getMessage());
        }
        return result;
    }

    @Override
    public ResultData queryFileMap(Map<String, Object> condition){
        ResultData result = new ResultData();
        List<FileMap> list = new ArrayList<>();
        try{
            list = sqlSession.selectList("gmair.resource.filemap.query", condition);
            result.setData(list);

        } catch (Exception e) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription(e.getMessage());
        }

        if(result.getResponseCode()!=ResponseCode.RESPONSE_ERROR) {
            if (list.isEmpty() == true) {
                result.setResponseCode(ResponseCode.RESPONSE_NULL);
                result.setDescription("No filemap found");
            } else {
                result.setResponseCode(ResponseCode.RESPONSE_OK);
                result.setDescription("Success to found filemap");
            }
        }
        return result;
    }
}
