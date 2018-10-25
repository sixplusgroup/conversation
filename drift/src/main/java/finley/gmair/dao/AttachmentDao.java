package finley.gmair.dao;

import finley.gmair.model.drift.Attachment;
import finley.gmair.util.ResultData;

import java.util.Map;

public interface AttachmentDao {
    ResultData query(Map<String, Object> condition);

    ResultData insert(Attachment attachment);

    ResultData update(Map<String, Object> condition);
}
