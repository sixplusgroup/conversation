package finley.gmair.dao.impl;

import finley.gmair.dao.AddressDao;
import finley.gmair.dao.BaseDao;
import finley.gmair.model.drift.DriftAddress;
import finley.gmair.util.IDGenerator;
import finley.gmair.util.ResponseCode;
import finley.gmair.util.ResultData;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public class AddressDaoImpl extends BaseDao implements AddressDao {

    @Override
    public ResultData query(Map<String, Object> condition) {
        ResultData result = new ResultData();
        try {
            List<DriftAddress> list = sqlSession.selectList("gmair.drift.address.query", condition);
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
    public ResultData insert(DriftAddress address) {
        ResultData result = new ResultData();
        address.setAddressId(IDGenerator.generate("DAI"));
        try {
            sqlSession.insert("gmair.drift.address.insert", address);
            result.setData(address);
        } catch (Exception e) {
            e.printStackTrace();
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription(e.getMessage());
        }
        return result;
    }

    @Override
    public ResultData update(DriftAddress address) {
        ResultData result = new ResultData();
        try {
            sqlSession.update("gmair.drift.address.update", address);
            result.setData(address);
        } catch (Exception e) {
            e.printStackTrace();
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription(e.getMessage());
        }
        return result;
    }
}
