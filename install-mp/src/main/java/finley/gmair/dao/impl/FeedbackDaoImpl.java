package finley.gmair.dao.impl;

import finley.gmair.dao.BaseDao;
import finley.gmair.dao.FeedbackDao;
import finley.gmair.model.installation.Feedback;
import finley.gmair.util.IDGenerator;
import finley.gmair.util.ResponseCode;
import finley.gmair.util.ResultData;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Repository
public class FeedbackDaoImpl extends BaseDao implements FeedbackDao {

    @Override
    public ResultData insertFeedback(Feedback feedback) {

        ResultData result = new ResultData();
        feedback.setFeedbackId(IDGenerator.generate("IFB"));
        try{
            sqlSession.insert("gmair.installation.feedback.insert",feedback);
            result.setData(feedback);
        }
        catch(Exception e){
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription(e.getMessage());
        }
        return result;
    }

    @Override
    public ResultData queryFeedback(Map<String, Object> condition){
        ResultData result = new ResultData();
        List<Feedback> list=new ArrayList<>();
        try{
            list=sqlSession.selectList("gmair.installation.feedback.query",condition);
            result.setData(list);
        }
        catch(Exception e){
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription(e.getMessage());
        }

        if(result.getResponseCode()!=ResponseCode.RESPONSE_ERROR) {
            if (list.isEmpty() == true) {
                result.setResponseCode(ResponseCode.RESPONSE_NULL);
                result.setDescription("No feedback found");
            } else {
                result.setResponseCode(ResponseCode.RESPONSE_OK);
                result.setDescription("Success to found feedback");
            }
        }
        return result;
    }

}
