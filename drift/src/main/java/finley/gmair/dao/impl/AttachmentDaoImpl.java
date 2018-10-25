package finley.gmair.dao.impl;

import finley.gmair.dao.AttachmentDao;
import finley.gmair.dao.BaseDao;
import finley.gmair.model.drift.Attachment;
import finley.gmair.util.IDGenerator;
import finley.gmair.util.ResponseCode;
import finley.gmair.util.ResultData;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public class AttachmentDaoImpl extends BaseDao implements AttachmentDao {

    @Override
    public ResultData query(Map<String, Object> condition) {
        ResultData result = new ResultData();
        try {
            List<Attachment> list = sqlSession.selectList("gmair.drift.attachment.query", condition);
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
    public ResultData insert(Attachment attachment) {
        ResultData result = new ResultData();
        attachment.setAttachId(IDGenerator.generate("DAI"));
        try {
            sqlSession.insert("gmair.drift.attachment.insert", attachment);
            result.setData(attachment);
        } catch (Exception e) {
            e.printStackTrace();
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription(e.getMessage());
        }
        return result;
    }

    @Override
    public ResultData update(Map<String, Object> condition) {
        ResultData result = new ResultData();
        try {
            sqlSession.update("gmair.drift.attachment.update", condition);
        } catch (Exception e) {
            e.printStackTrace();
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription(e.getMessage());
        }
        return result;
    }
}
