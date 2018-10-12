package finley.gmair.dao.Impl;

import finley.gmair.dao.SnapshotDao;
import finley.gmair.model.assemble.Snapshot;
import finley.gmair.util.IDGenerator;
import finley.gmair.util.ResponseCode;
import finley.gmair.util.ResultData;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public class SnapshotDaoImpl extends BaseDao implements SnapshotDao {

    @Override
    public ResultData insert(Snapshot snapshot) {
        ResultData result = new ResultData();
        snapshot.setSnapshotId(IDGenerator.generate("SNP"));
        try {
            sqlSession.insert("gmair.assemble.snapshot.insert", snapshot);
            result.setData(snapshot);
        } catch (Exception e) {
            e.printStackTrace();
            result.setDescription(e.getMessage());
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
        }
        return result;
    }

    @Override
    public ResultData query(Map<String, Object> condition) {
        ResultData result = new ResultData();
        try {
            List<Snapshot> list = sqlSession.selectList("gmair.assemble.snapshot.query", condition);
            if (list.isEmpty()) {
                result.setResponseCode(ResponseCode.RESPONSE_NULL);
            }
            result.setData(list);
        } catch (Exception e) {
            e.printStackTrace();
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription(e.getMessage());
        }
        return result;
    }

    @Override
    public ResultData update(Map<String, Object> condition){
        ResultData result = new ResultData();
        try{
            sqlSession.update("gmair.assemble.snapshot.update",condition);
        }catch (Exception e){
            e.printStackTrace();
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription(e.getMessage());
        }
        return result;
    }
}
