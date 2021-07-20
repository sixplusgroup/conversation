package finley.gmair.dao;

import finley.gmair.model.wechat.ReplyRecord;

import java.util.List;
import java.util.Map;

/**
 * @ClassName: ReplyRecordDao
 * @Description: TODO
 * @Author fan
 * @Date 2021/1/10 4:45 PM
 */
public interface ReplyRecordDao {
    boolean insert(ReplyRecord record);

    List<ReplyRecord> query(Map<String, Object> condition);
}
