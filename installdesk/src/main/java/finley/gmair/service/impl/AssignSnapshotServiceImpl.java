package finley.gmair.service.impl;

import finley.gmair.dao.AssignSnapshotDao;
import finley.gmair.model.installation.Snapshot;
import finley.gmair.service.AssignSnapshotService;
import finley.gmair.util.ResponseCode;
import finley.gmair.util.ResultData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * @ClassName: AssignSnapshotServiceImpl
 * @Description: TODO
 * @Author fan
 * @Date 2019/4/19 2:43 PM
 */
@Service
public class AssignSnapshotServiceImpl implements AssignSnapshotService {

    @Autowired
    private AssignSnapshotDao assignSnapshotDao;

    @Override
    public ResultData create(Snapshot snapshot) {
        ResultData result = new ResultData();
        ResultData response = assignSnapshotDao.insert(snapshot);
        if (response.getResponseCode() == ResponseCode.RESPONSE_ERROR) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("创建安装任务快照失败，请稍后尝试");
            return result;
        }
        result.setData(response.getData());
        return result;
    }

    @Override
    public ResultData fetch(Map<String, Object> condition) {
        ResultData result = new ResultData();
        ResultData response = assignSnapshotDao.query(condition);
        if (response.getResponseCode() == ResponseCode.RESPONSE_ERROR) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("查询安装任务快照失败，请稍后尝试");
            return result;
        }
        if (response.getResponseCode() == ResponseCode.RESPONSE_NULL) {
            result.setResponseCode(ResponseCode.RESPONSE_NULL);
            result.setDescription("未能查寻到相关的安装任务快照");
            return result;
        }
        result.setData(response.getData());
        return result;
    }

    @Override
    public ResultData block(String snapshotId) {
        ResultData result = new ResultData();
        ResultData response = assignSnapshotDao.block(snapshotId);
        if (response.getResponseCode() == ResponseCode.RESPONSE_ERROR) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("屏蔽安装任务快照失败，请稍后尝试");
            return result;
        }
        result.setDescription("屏蔽安装任务快照成功");
        return result;
    }

    @Override
    public ResultData remove(String snapshotId) {
        ResultData result = new ResultData();
        ResultData response = assignSnapshotDao.remove(snapshotId);
        if (response.getResponseCode() == ResponseCode.RESPONSE_ERROR) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("删除安装任务快照失败，请稍后尝试");
            return result;
        }
        result.setDescription("删除安装任务快照成功");
        return result;
    }
}
