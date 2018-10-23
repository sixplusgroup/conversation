package finley.gmair.dao.impl;

import finley.gmair.dao.BaseDao;
import finley.gmair.dao.SnapshotDao;
import finley.gmair.model.installation.Snapshot;
import finley.gmair.util.ResultData;
import org.springframework.stereotype.Repository;

import java.util.Map;

@Repository
public class SnapshotDaoImpl extends BaseDao implements SnapshotDao {

    @Override
    public ResultData query(Map<String,Object> condition) { return  null; }

    @Override
    public ResultData insert(Snapshot snapshot) {return  null; }

    @Override
    public ResultData update(Snapshot snapshot) {return null; }
}
