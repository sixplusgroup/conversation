package finley.gmair.service.impl;

import finley.gmair.dao.SurveySnapshotDao;
import finley.gmair.model.installation.SnapshotSurvey;
import finley.gmair.service.SurveySnapshotService;
import finley.gmair.util.ResponseCode;
import finley.gmair.util.ResultData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class SurveySnapshotServiceImpl implements SurveySnapshotService {

    @Autowired
    private SurveySnapshotDao surveySnapshotDao;

    @Override
    public ResultData create(SnapshotSurvey snapshot) {
        ResultData result = new ResultData();
        ResultData response = surveySnapshotDao.insert(snapshot);
        if (response.getResponseCode() == ResponseCode.RESPONSE_ERROR) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("创建勘测任务快照失败，请稍后尝试");
            return result;
        }
        result.setData(response.getData());
        return result;
    }

    @Override
    public ResultData fetch(Map<String, Object> condition) {
        ResultData result = new ResultData();
        ResultData response = surveySnapshotDao.query(condition);
        if (response.getResponseCode() == ResponseCode.RESPONSE_ERROR) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("查询勘测任务快照失败，请稍后尝试");
            return result;
        }
        if (response.getResponseCode() == ResponseCode.RESPONSE_NULL) {
            result.setResponseCode(ResponseCode.RESPONSE_NULL);
            result.setDescription("未能查寻到相关的勘测任务快照");
            return result;
        }
        result.setData(response.getData());
        return result;
    }

    @Override
    public ResultData block(String snapshotId) {
        ResultData result = new ResultData();
        ResultData response = surveySnapshotDao.block(snapshotId);
        if (response.getResponseCode() == ResponseCode.RESPONSE_ERROR) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("屏蔽勘测任务快照失败，请稍后尝试");
            return result;
        }
        result.setDescription("屏蔽勘测任务快照成功");
        return result;
    }

    @Override
    public ResultData remove(String snapshotId) {
        ResultData result = new ResultData();
        ResultData response = surveySnapshotDao.remove(snapshotId);
        if (response.getResponseCode() == ResponseCode.RESPONSE_ERROR) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("删除勘测任务快照失败，请稍后尝试");
            return result;
        }
        result.setDescription("删除勘测任务快照成功");
        return result;
    }
}
