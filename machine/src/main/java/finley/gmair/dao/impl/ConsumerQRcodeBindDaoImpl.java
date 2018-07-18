package finley.gmair.dao.impl;

import finley.gmair.dao.BaseDao;
import finley.gmair.dao.ConsumerQRcodeBindDao;
import finley.gmair.model.machine.ConsumerQRcodeBind;
import finley.gmair.util.IDGenerator;
import finley.gmair.util.ResponseCode;
import finley.gmair.util.ResultData;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public class ConsumerQRcodeBindDaoImpl extends BaseDao implements ConsumerQRcodeBindDao {

    @Override
    public ResultData insert(ConsumerQRcodeBind consumerQRcodeBind){
        ResultData result = new ResultData();
        consumerQRcodeBind.setBindId(IDGenerator.generate("CQB"));
        try {
            sqlSession.insert("gmair.machine.consumer_qrcode_bind.insert", consumerQRcodeBind);
            result.setData(consumerQRcodeBind);
        } catch (Exception e) {
            e.printStackTrace();
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription(e.getMessage());
        }
        return result;
    }

    @Override
    public ResultData query(Map<String,Object> condition){
        ResultData result = new ResultData();
        try {
            List<ConsumerQRcodeBind> list = sqlSession.selectList("gmair.machine.consumer_qrcode_bind.query", condition);
            if (list.isEmpty()) {
                result.setResponseCode(ResponseCode.RESPONSE_NULL);
            }
            result.setData(list);
        } catch (Exception e){
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
            sqlSession.update("gmair.machine.consumer_qrcode_bind.update", condition);
        }catch (Exception e){
            e.printStackTrace();
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription(e.getMessage());
        }
        return result;
    }
}
