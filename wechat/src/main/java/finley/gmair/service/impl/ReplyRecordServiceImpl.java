package finley.gmair.service.impl;

import finley.gmair.dao.ReplyRecordDao;
import finley.gmair.model.wechat.ReplyRecord;
import finley.gmair.service.ReplyRecordService;
import finley.gmair.util.ResponseCode;
import finley.gmair.util.ResultData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * @ClassName: ReplyRecordServiceImpl
 * @Description: TODO
 * @Author fan
 * @Date 2021/1/10 9:07 PM
 */
@Service
public class ReplyRecordServiceImpl implements ReplyRecordService {
    private Logger logger = LoggerFactory.getLogger(ReplyRecordServiceImpl.class);

    @Resource
    private ReplyRecordDao replyRecordDao;

    @Override
    public ResultData create(ReplyRecord record) {
        ResultData result = new ResultData();
        if (replyRecordDao.insert(record)) {
            result.setDescription("success to save record");
        } else {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("fail to save record");
        }
        return result;
    }

    @Override
    public ResultData fetch(Map<String, Object> condition) {
        ResultData result = new ResultData();
        List<ReplyRecord> list = replyRecordDao.query(condition);
        result.setData(list);
        if (list.isEmpty() || list.size() == 0) {
            result.setResponseCode(ResponseCode.RESPONSE_NULL);
        }
        return result;
    }
}
