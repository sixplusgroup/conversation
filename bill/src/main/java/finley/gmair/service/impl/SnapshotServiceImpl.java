package finley.gmair.service.impl;

import finley.gmair.dao.SnapshotDao;
import finley.gmair.model.bill.DealSnapshot;
import finley.gmair.service.SnapshotService;
import finley.gmair.util.ResponseCode;
import finley.gmair.util.ResultData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class SnapshotServiceImpl implements SnapshotService {

    @Autowired
    private SnapshotDao snapshotDao;

    @Override
    public ResultData fetchSnapshot(Map<String, Object> condition) {
        ResultData result = new ResultData();
        ResultData response = snapshotDao.query(condition);
        switch (response.getResponseCode()) {
            case RESPONSE_NULL:
                result.setResponseCode(ResponseCode.RESPONSE_NULL);
                result.setDescription("No snapshot found");
                break;
            case RESPONSE_ERROR:
                result.setResponseCode(ResponseCode.RESPONSE_ERROR);
                result.setDescription("Fail to query snapshot");
                break;
            case RESPONSE_OK:
                result.setResponseCode(ResponseCode.RESPONSE_OK);
                result.setData(response.getData());
                break;
        }
        return result;
    }

    @Override
    public ResultData createSnapshot(DealSnapshot snapshot) {

        ResultData result = new ResultData();
        ResultData response = snapshotDao.insert(snapshot);
        if (response.getResponseCode() != ResponseCode.RESPONSE_OK) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("Fail to insert snapshot with: " + snapshot.toString());
            return result;
        }
        result.setResponseCode(ResponseCode.RESPONSE_OK);
        result.setData(response.getData());
        return result;
    }

    @Override
    public ResultData deleteSnapshot(String snapshotId) {
        ResultData result = new ResultData();
        Map<String, Object> condition = new HashMap<>();
        condition.put("snapshotId",snapshotId);
        ResultData response = snapshotDao.query(condition);
        if (response.getResponseCode() != ResponseCode.RESPONSE_OK) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("Fail to query snapshot");
            return result;
        }
        result = snapshotDao.delete(snapshotId);
        return result;
    }
}
