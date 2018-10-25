package finley.gmair.service;

import finley.gmair.model.drift.Attachment;
import finley.gmair.util.ResultData;

import java.util.Map;

public interface AttachmentService {

    ResultData fetch(Map<String, Object> condition);

    ResultData create(Attachment attachment);

    ResultData modify(Map<String, Object> condition);
}
