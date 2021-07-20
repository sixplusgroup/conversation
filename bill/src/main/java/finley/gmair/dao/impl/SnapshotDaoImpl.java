package finley.gmair.dao.impl;

import finley.gmair.dao.BaseDao;
import finley.gmair.dao.SnapshotDao;
import finley.gmair.model.bill.DealSnapshot;
import finley.gmair.util.IDGenerator;
import finley.gmair.util.ResponseCode;
import finley.gmair.util.ResultData;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public class SnapshotDaoImpl extends BaseDao implements SnapshotDao {

    @Override
    public ResultData query(Map<String, Object> condition) {
        ResultData result = new ResultData();
        try{
            List<DealSnapshot> list = sqlSession.selectList("gmair.bill.snapshot.query", condition);
            if (list.isEmpty()) {
                result.setResponseCode(ResponseCode.RESPONSE_NULL);
            }
            result.setData(list);
        }
        catch (Exception e) {
            e.printStackTrace();
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription(e.getMessage());
        }
        return result;

    }

    @Override
    public ResultData insert(DealSnapshot snapshot) {

        ResultData result = new ResultData();
        snapshot.setSnapshotId(IDGenerator.generate("SNAP"));
        try {
            sqlSession.insert("gmair.bill.snapshot.insert", snapshot);
            result.setData(snapshot);
        } catch (Exception e) {
            e.printStackTrace();
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription(e.getMessage());
        }
        return result;
    }

    @Override
    public ResultData delete(String snapshotId) {
        ResultData result = new ResultData();
        try {
            sqlSession.delete("gmair.bill.snapshot.delete", snapshotId);
        } catch (Exception e) {
            e.printStackTrace();
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription(e.getMessage());
        }
        return result;
    }

}
