package finley.gmair.service.impl;

import finley.gmair.dao.FixSnapshotDao;
import finley.gmair.model.installation.SnapshotFix;
import finley.gmair.service.FixSnapshotService;
import finley.gmair.util.ResponseCode;
import finley.gmair.util.ResultData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class FixSnapshotServiceImpl implements FixSnapshotService {

    @Autowired
    private FixSnapshotDao fixSnapshotDao;

    @Override
    public ResultData create(SnapshotFix snapshot) {
        ResultData result = new ResultData();
        ResultData response = fixSnapshotDao.insert(snapshot);
        if (response.getResponseCode() == ResponseCode.RESPONSE_ERROR) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("创建维修任务快照失败，请稍后尝试");
            return result;
        }
        result.setData(response.getData());
        return result;
    }

    @Override
    public ResultData fetch(Map<String, Object> condition) {
        ResultData result = new ResultData();
        ResultData response = fixSnapshotDao.query(condition);
        if (response.getResponseCode() == ResponseCode.RESPONSE_ERROR) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("查询维修任务快照失败，请稍后尝试");
            return result;
        }
        if (response.getResponseCode() == ResponseCode.RESPONSE_NULL) {
            result.setResponseCode(ResponseCode.RESPONSE_NULL);
            result.setDescription("未能查寻到相关的维修任务快照");
            return result;
        }
        result.setData(response.getData());
        return result;
    }

    @Override
    public ResultData block(String snapshotId) {
        ResultData result = new ResultData();
        ResultData response = fixSnapshotDao.block(snapshotId);
        if (response.getResponseCode() == ResponseCode.RESPONSE_ERROR) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("屏蔽维修任务快照失败，请稍后尝试");
            return result;
        }
        result.setDescription("屏蔽维修任务快照成功");
        return result;
    }

    @Override
    public ResultData remove(String snapshotId) {
        ResultData result = new ResultData();
        ResultData response = fixSnapshotDao.remove(snapshotId);
        if (response.getResponseCode() == ResponseCode.RESPONSE_ERROR) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("删除维修任务快照失败，请稍后尝试");
            return result;
        }
        result.setDescription("删除维修任务快照成功");
        return result;
    }
}
