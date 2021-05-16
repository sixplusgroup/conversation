package finley.gmair.dao.impl;

import finley.gmair.dao.AddressDao;
import finley.gmair.dao.BaseDao;
import finley.gmair.model.consumer.Address;
import finley.gmair.util.IDGenerator;
import finley.gmair.util.ResponseCode;
import finley.gmair.util.ResultData;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;

@Component
public class AddressDaoImpl extends BaseDao implements AddressDao {

    @Override
    @Transactional
    public ResultData insert(Address address, String consumerId) {
        ResultData result = new ResultData();
        address.setAddressId(IDGenerator.generate("ADR"));
        Map<String, Object> value = new TreeMap<>();
        value.put("address", address);
        value.put("consumerId", consumerId);
        try {
            sqlSession.insert("gmair.consumer.address.insert", value);
            result.setData(address);
        } catch (Exception e) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription(e.getMessage());
        }
        return result;
    }

    @Override
    public ResultData update(Map<String, Object> condition) {
        ResultData result = new ResultData();
        try {
            sqlSession.update("gmair.consumer.address.update", condition);
        } catch (Exception e) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription(e.getMessage());
        }
        return result;
    }

    @Override
    public ResultData query(Map<String, Object> condition) {
        ResultData result = new ResultData();
        try {
            List<Address> list = sqlSession.selectList("gmair.consumer.address.query", condition);
            if (list.isEmpty()) {
                result.setResponseCode(ResponseCode.RESPONSE_NULL);
            }
            result.setData(list);
        } catch (Exception e) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription(e.getMessage());
        }
        return result;
    }
}
