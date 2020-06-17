package finley.gmair.service.impl;

import finley.gmair.dao.ChangeMachineSnapshotDao;
import finley.gmair.model.installation.SnapshotChangeMachine;
import finley.gmair.service.ChangeMachineSnapshotService;
import finley.gmair.util.ResponseCode;
import finley.gmair.util.ResultData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class ChangeMachineSnapshotServiceImpl implements ChangeMachineSnapshotService {

    @Autowired
    private ChangeMachineSnapshotDao changeMachineSnapshotDao;

    @Override
    public ResultData create(SnapshotChangeMachine snapshot) {
        ResultData result = new ResultData();
        ResultData response = changeMachineSnapshotDao.insert(snapshot);
        if (response.getResponseCode() == ResponseCode.RESPONSE_ERROR) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("创建换机任务快照失败，请稍后尝试");
            return result;
        }
        result.setData(response.getData());
        return result;
    }

    @Override
    public ResultData fetch(Map<String, Object> condition) {
        ResultData result = new ResultData();
        ResultData response = changeMachineSnapshotDao.query(condition);
        if (response.getResponseCode() == ResponseCode.RESPONSE_ERROR) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("查询换机任务快照失败，请稍后尝试");
            return result;
        }
        if (response.getResponseCode() == ResponseCode.RESPONSE_NULL) {
            result.setResponseCode(ResponseCode.RESPONSE_NULL);
            result.setDescription("未能查寻到相关的换机任务快照");
            return result;
        }
        result.setData(response.getData());
        return result;
    }

    @Override
    public ResultData block(String snapshotId) {
        ResultData result = new ResultData();
        ResultData response = changeMachineSnapshotDao.block(snapshotId);
        if (response.getResponseCode() == ResponseCode.RESPONSE_ERROR) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("屏蔽换机任务快照失败，请稍后尝试");
            return result;
        }
        result.setDescription("屏蔽换机任务快照成功");
        return result;
    }

    @Override
    public ResultData remove(String snapshotId) {
        ResultData result = new ResultData();
        ResultData response = changeMachineSnapshotDao.remove(snapshotId);
        if (response.getResponseCode() == ResponseCode.RESPONSE_ERROR) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("删除换机任务快照失败，请稍后尝试");
            return result;
        }
        result.setDescription("删除换机任务快照成功");
        return result;
    }
}
