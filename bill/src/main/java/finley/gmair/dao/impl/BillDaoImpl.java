package finley.gmair.dao.impl;

import finley.gmair.dao.BaseDao;
import finley.gmair.dao.BillDao;
import finley.gmair.model.bill.BillInfo;
import finley.gmair.util.ResultData;
import org.springframework.stereotype.Repository;

import java.util.Map;


@Repository
public class BillDaoImpl extends BaseDao implements BillDao {

    @Override
    public ResultData query(Map<String, Object> condition) {
        return null;
    }

    @Override
    public ResultData insert(BillInfo billInfo) {
        return null;
    }

    @Override
    public ResultData update(BillInfo billInfo) {
        return null;
    }
}
