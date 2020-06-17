package finley.gmair.dao.impl;

import finley.gmair.dao.BaseDao;
import finley.gmair.dao.ChangeMachineSnapshotDao;
import finley.gmair.model.installation.SnapshotChangeMachine;
import finley.gmair.util.IDGenerator;
import finley.gmair.util.ResponseCode;
import finley.gmair.util.ResultData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public class ChangeMachineSnapshotDaoImpl extends BaseDao implements ChangeMachineSnapshotDao {
    private Logger logger = LoggerFactory.getLogger(AssignSnapshotDaoImpl.class);

    @Override
    public ResultData insert(SnapshotChangeMachine snapshot) {
        ResultData result = new ResultData();
        snapshot.setSnapshotId(IDGenerator.generate("SNT"));
        try {
            sqlSession.insert("gmair.install.assign.snapshotChangeMachine.insert", snapshot);
            result.setData(snapshot);
        } catch (Exception e) {
            logger.error(e.getMessage());
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription(e.getMessage());
        }
        return result;
    }

    @Override
    public ResultData query(Map<String, Object> condition) {
        ResultData result = new ResultData();
        try {
            List list = sqlSession.selectList("gmair.install.assign.snapshotChangeMachine.query", condition);
            if (list.isEmpty()) {
                result.setResponseCode(ResponseCode.RESPONSE_NULL);
            }
            result.setData(list);
        } catch (Exception e) {
            logger.error(e.getMessage());
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription(e.getMessage());
        }
        return result;
    }

    @Override
    public ResultData block(String snapshotId) {
        ResultData result = new ResultData();
        try {
            sqlSession.update("gmair.install.assign.snapshotChangeMachine.block", snapshotId);
        } catch (Exception e) {
            logger.error(e.getMessage());
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription(e.getMessage());
        }
        return result;
    }

    @Override
    public ResultData remove(String snapshotId) {
        ResultData result = new ResultData();
        try {
            sqlSession.update("gmair.install.assign.snapshotChangeMachine.remove", snapshotId);
        } catch (Exception e) {
            logger.error(e.getMessage());
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription(e.getMessage());
        }
        return result;
    }
}
