package finley.gmair.dao.impl;

import com.alibaba.fastjson.JSON;
import finley.gmair.dao.BaseDao;
import finley.gmair.dao.ReplyRecordDao;
import finley.gmair.model.wechat.ReplyRecord;
import finley.gmair.util.IDGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @ClassName: ReplyRecordDaoImpl
 * @Description: TODO
 * @Author fan
 * @Date 2021/1/10 8:13 PM
 */
@Repository
public class ReplyRecordDaoImpl extends BaseDao implements ReplyRecordDao {
    private Logger logger = LoggerFactory.getLogger(ReplyRecordDaoImpl.class);

    @Override
    public boolean insert(ReplyRecord record) {
        record.setRecordId(IDGenerator.generate("RRD"));
        try {
            sqlSession.insert("gmair.wechat.reply.record.insert", record);
            return true;
        } catch (Exception e) {
            logger.error("[Error] fail to save record: " + JSON.toJSONString(record) + " to database");
            logger.error(e.getMessage());
        }
        return false;
    }

    @Override
    public List<ReplyRecord> query(Map<String, Object> condition) {
        List<ReplyRecord> result = new ArrayList<>();
        try {
            result = sqlSession.selectList("gmair.wechat.reply.record.query", condition);
        } catch (Exception e) {
            logger.error("[Error] fail to retrieve record with condition: " + JSON.toJSONString(condition));
            logger.error(e.getMessage());
        }
        return result;
    }
}
