package finley.gmair.dao.impl;

import finley.gmair.dao.BaseDao;
import finley.gmair.dao.PhoneDao;
import finley.gmair.model.consumer.Phone;
import finley.gmair.util.IDGenerator;
import finley.gmair.util.ResponseCode;
import finley.gmair.util.ResultData;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class PhoneDaoImpl extends BaseDao implements PhoneDao {

    @Override
    @Transactional
    public ResultData insert(Phone phone, String consumerId) {
        ResultData result = new ResultData();
        phone.setPhoneId(IDGenerator.generate("PHN"));
        Map<String, Object> value = new HashMap<>();
        value.put("phone", phone);
        value.put("consumerId", consumerId);
        try {
            sqlSession.insert("gmair.consumer.phone.insert", value);
            result.setData(phone);
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
            List<Phone> list = sqlSession.selectList("gmair.consumer.phone.query", condition);
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

    @Override
    public ResultData update(Map<String, Object> condition) {
        ResultData result = new ResultData();

        try {
            sqlSession.update("gmair.consumer.phone.update", condition);
            result.setResponseCode(ResponseCode.RESPONSE_OK);
        } catch (Exception e) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription(e.getMessage());
        }
        return result;
    }
}
