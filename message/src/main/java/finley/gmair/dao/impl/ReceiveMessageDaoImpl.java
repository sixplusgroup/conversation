package finley.gmair.dao.impl;

import finley.gmair.dao.BaseDao;
import finley.gmair.dao.ReceiveMessageDao;
import finley.gmair.model.message.TextMessage;
import finley.gmair.util.IDGenerator;
import finley.gmair.util.ResponseCode;
import finley.gmair.util.ResultData;
import org.apache.ibatis.session.RowBounds;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Repository
public class ReceiveMessageDaoImpl extends BaseDao implements ReceiveMessageDao {

    @Override
    public ResultData insert(TextMessage message) {
        ResultData result = new ResultData();
        message.setMessageId(IDGenerator.generate("ITM"));
        try {
            sqlSession.insert("gmair.message.receive.insert", message);
            result.setData(message);
        } catch (Exception e) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription(e.getMessage());
            System.out.println("[Error] " + e.getMessage());
        }
        return result;
    }

    @Override
    public ResultData query(Map<String, Object> condition) {
        ResultData result = new ResultData();
        try {
            List<TextMessage> list = new ArrayList<>();
//            if (condition.get("pagesize")!=null&&condition.get("pageno")!=null){
//                list = sqlSession.selectList("gmair.message.receive.query", condition,new RowBounds(Integer.parseInt(String.valueOf(condition.get("pageno"))),Integer.parseInt(String.valueOf(condition.get("pagesize")))));
//            }else{
                list = sqlSession.selectList("gmair.message.receive.query", condition);
//            }
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
