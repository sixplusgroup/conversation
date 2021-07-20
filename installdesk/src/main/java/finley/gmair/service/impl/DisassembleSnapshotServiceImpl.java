package finley.gmair.service.impl;

import finley.gmair.dao.DisassembleSnapshotDao;
import finley.gmair.model.installation.SnapshotDisassemble;
import finley.gmair.service.DisassembleSnapshotService;
import finley.gmair.util.ResponseCode;
import finley.gmair.util.ResultData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class DisassembleSnapshotServiceImpl implements DisassembleSnapshotService {

    @Autowired
    private DisassembleSnapshotDao disassembleSnapshotDao;

    @Override
    public ResultData create(SnapshotDisassemble snapshot) {
        ResultData result = new ResultData();
        ResultData response = disassembleSnapshotDao.insert(snapshot);
        if (response.getResponseCode() == ResponseCode.RESPONSE_ERROR) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("创建拆机任务快照失败，请稍后尝试");
            return result;
        }
        result.setData(response.getData());
        return result;
    }

    @Override
    public ResultData fetch(Map<String, Object> condition) {
        ResultData result = new ResultData();
        ResultData response = disassembleSnapshotDao.query(condition);
        if (response.getResponseCode() == ResponseCode.RESPONSE_ERROR) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("查询拆机任务快照失败，请稍后尝试");
            return result;
        }
        if (response.getResponseCode() == ResponseCode.RESPONSE_NULL) {
            result.setResponseCode(ResponseCode.RESPONSE_NULL);
            result.setDescription("未能查寻到相关的拆机任务快照");
            return result;
        }
        result.setData(response.getData());
        return result;
    }

    @Override
    public ResultData block(String snapshotId) {
        ResultData result = new ResultData();
        ResultData response = disassembleSnapshotDao.block(snapshotId);
        if (response.getResponseCode() == ResponseCode.RESPONSE_ERROR) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("屏蔽拆机任务快照失败，请稍后尝试");
            return result;
        }
        result.setDescription("屏蔽拆机任务快照成功");
        return result;
    }

    @Override
    public ResultData remove(String snapshotId) {
        ResultData result = new ResultData();
        ResultData response = disassembleSnapshotDao.remove(snapshotId);
        if (response.getResponseCode() == ResponseCode.RESPONSE_ERROR) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("删除拆机任务快照失败，请稍后尝试");
            return result;
        }
        result.setDescription("删除拆机任务快照成功");
        return result;
    }
}
