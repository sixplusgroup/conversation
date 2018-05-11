package finley.gmair.dao.impl;

import finley.gmair.dao.BaseDao;
import finley.gmair.dao.SnapshotLocDao;
import finley.gmair.model.installation.SnapshotLoc;
import finley.gmair.util.IDGenerator;
import finley.gmair.util.ResponseCode;
import finley.gmair.util.ResultData;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Repository
public class SnapshotLocDaoImpl extends BaseDao implements SnapshotLocDao {

    @Override
    public ResultData insertSnapshotLoc(SnapshotLoc snapshotLoc) {
        ResultData result = new ResultData();
        snapshotLoc.setLocationId(IDGenerator.generate("SLC"));
        try {
            sqlSession.insert("gmair.installation.snapshotloc.insert", snapshotLoc);
            result.setData(snapshotLoc);
        } catch (Exception e) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription(e.getMessage());
        }
        return result;
    }

    @Override
    public ResultData querySnapshotLoc(Map<String, Object> condition) {
        ResultData result = new ResultData();
        List<SnapshotLoc> list = new ArrayList<>();
        try {
            list = sqlSession.selectList("gmair.installation.snapshotloc.query", condition);
            result.setData(list);
        } catch (Exception e) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription(e.getMessage());
        }

        if (result.getResponseCode() != ResponseCode.RESPONSE_ERROR) {
            if (list.isEmpty() == true) {
                result.setResponseCode(ResponseCode.RESPONSE_NULL);
                result.setDescription("No snapshotloc found");
            } else {
                result.setResponseCode(ResponseCode.RESPONSE_OK);
                result.setDescription("Success to found snapshotloc");
            }
        }
        return result;
    }

}
