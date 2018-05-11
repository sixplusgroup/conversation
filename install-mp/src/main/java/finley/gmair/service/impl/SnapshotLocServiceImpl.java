package finley.gmair.service.impl;

import finley.gmair.dao.SnapshotLocDao;
import finley.gmair.model.installation.Snapshot;
import finley.gmair.model.installation.SnapshotLoc;
import finley.gmair.service.SnapshotLocService;
import finley.gmair.util.ResponseCode;
import finley.gmair.util.ResultData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class SnapshotLocServiceImpl implements SnapshotLocService {

    @Autowired
    private SnapshotLocDao snapshotLocDao;

    @Override
    public ResultData createSnapshotLoc(SnapshotLoc snapshotLoc) {
        ResultData result = new ResultData();
        ResultData response = snapshotLocDao.insertSnapshotLoc(snapshotLoc);
        if(response.getResponseCode() == ResponseCode.RESPONSE_OK){
            result.setResponseCode(ResponseCode.RESPONSE_OK);
            result.setData(response.getData());
        }
        else if(response.getResponseCode() == ResponseCode.RESPONSE_ERROR){
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("Fail to insert snapshotloc" + snapshotLoc.toString());
        }
        return result;
    }

    @Override
    public ResultData fetchSnapshotLoc(Map<String, Object> condition){
        ResultData result = new ResultData();
        ResultData response = snapshotLocDao.querySnapshotLoc(condition);
        if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
            result.setResponseCode(ResponseCode.RESPONSE_OK);
            result.setData(response.getData());
            result.setDescription("Success to fetch snapshot location");
        }
        else if (response.getResponseCode() == ResponseCode.RESPONSE_NULL) {
            result.setResponseCode(ResponseCode.RESPONSE_NULL);
            result.setDescription("No snapshot location found");
        }
        else if (response.getResponseCode() == ResponseCode.RESPONSE_ERROR) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("Fail to fetch snapshot location");
        }
        return result;
    }
}
