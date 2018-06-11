package finley.gmair.dao.impl;

import finley.gmair.dao.BaseDao;
import finley.gmair.dao.SnapshotDao;
import finley.gmair.model.installation.Snapshot;
import finley.gmair.util.IDGenerator;
import finley.gmair.util.ResponseCode;
import finley.gmair.util.ResultData;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Repository
public class SnapshotDaoImpl extends BaseDao implements SnapshotDao {

    @Override
    public ResultData insertSnapshot(Snapshot snapshot) {
        ResultData result = new ResultData();
        snapshot.setSnapshotId(IDGenerator.generate("ISS"));
        try{
            sqlSession.insert("gmair.installation.snapshot.insert",snapshot);
            result.setData(snapshot);
        }
        catch(Exception e){
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription(e.getMessage());
            System.out.println(new StringBuffer("Snapshot create failure: ").append(e.getMessage()).toString());
        }
        return result;
    }

    @Override
    public ResultData querySnapshot(Map<String, Object> condition){
        ResultData result = new ResultData();
        List<Snapshot> list=new ArrayList<>();
        try{
            list=sqlSession.selectList("gmair.installation.snapshot.query",condition);
            result.setData(list);
        }
        catch(Exception e){
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription(e.getMessage());
        }

        if(result.getResponseCode()!=ResponseCode.RESPONSE_ERROR) {
            if (list.isEmpty() == true) {
                result.setResponseCode(ResponseCode.RESPONSE_NULL);
                result.setDescription("No snapshot found");
            } else {
                result.setResponseCode(ResponseCode.RESPONSE_OK);
                result.setDescription("Success to found snapshot");
            }
        }
        return result;
    }
}
