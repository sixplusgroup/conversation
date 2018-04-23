package finley.gmair.service.impl;

import finley.gmair.dao.SnapshotDao;
import finley.gmair.model.installation.Snapshot;
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
    public ResultData createSnapshot(Snapshot snapshot)
    {
        ResultData result = new ResultData();
        ResultData response = snapshotDao.insertSnapshot(snapshot);
        if(response.getResponseCode() == ResponseCode.RESPONSE_OK){
            result.setResponseCode(ResponseCode.RESPONSE_OK);
            result.setData(response.getData());
        }
        else if(response.getResponseCode() == ResponseCode.RESPONSE_ERROR){
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("Fail to insert snapshot" + snapshot.toString());
        }
        return result;
    }

    @Override
    public ResultData fetchSnapshot(Map<String, Object> condition){
        ResultData result = new ResultData();
        ResultData response = snapshotDao.querySnapshot(condition);
        if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
            result.setResponseCode(ResponseCode.RESPONSE_OK);
            result.setData(response.getData());
            result.setDescription("Success to fetch snapshot");
        }
        if (response.getResponseCode() == ResponseCode.RESPONSE_NULL) {
            result.setResponseCode(ResponseCode.RESPONSE_NULL);
            result.setDescription("No snapshot found");
        }
        if (response.getResponseCode() == ResponseCode.RESPONSE_ERROR) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("Fail to fetch snapshot");
        }
        return result;
    }

}
