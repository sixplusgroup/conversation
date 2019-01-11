package finley.gmair.service.impl;

import finley.gmair.dao.SnapshotDao;
import finley.gmair.model.assemble.Snapshot;
import finley.gmair.service.SnapshotService;
import finley.gmair.util.ResponseCode;
import finley.gmair.util.ResultData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class SnapshotServiceImpl implements SnapshotService {
    @Autowired
    private SnapshotDao snapshotDao;

    @Override
    public ResultData create(Snapshot snapshot){
        ResultData result = new ResultData();
        ResultData response = snapshotDao.insert(snapshot);
        if (response.getResponseCode() == ResponseCode.RESPONSE_ERROR) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription(new StringBuffer("Fail to insert snapshot into database.").toString());
            return result;
        }
        result.setData(response.getData());
        return result;
    }

    @Override
    public ResultData fetch(Map<String, Object> condition){
        ResultData result = new ResultData();
        ResultData response = snapshotDao.query(condition);
        if (response.getResponseCode() == ResponseCode.RESPONSE_ERROR) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("Fail to fetch snapshot from database.");
            return result;
        }
        else if (response.getResponseCode() == ResponseCode.RESPONSE_NULL) {
            result.setResponseCode(ResponseCode.RESPONSE_NULL);
            result.setDescription("No snapshot found.");
            return result;
        }
        result.setData(response.getData());
        result.setDescription("success to find snapshot");
        return result;
    }

    @Override
    public ResultData update(Map<String, Object> condition) {
        ResultData result = new ResultData();
        ResultData response = snapshotDao.update(condition);
        if(response.getResponseCode() == ResponseCode.RESPONSE_ERROR){
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            return result;
        }
        return result;
    }
}
