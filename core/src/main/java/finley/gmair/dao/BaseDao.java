package finley.gmair.dao;


import finley.gmair.converter.TimestampConverter;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.data.mongodb.core.MongoTemplate;

import javax.annotation.Resource;
import java.sql.Timestamp;

/**
 * 该类用于管理持久层与数据库的连接monge template, 所有的Dao需要继承此方法
 * Created by xwz on 5/8/18.
 */
public class BaseDao {

    @Resource
    protected MongoDbFactory mongoDbFactory;

    @Resource
    protected MongoTemplate mongoTemplate;

    @Resource
    protected TimestampConverter timestampConverter;

    public MongoTemplate getMongoTemplate() {
        return mongoTemplate;
    }

    public void setMongoTemplate(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    public TimestampConverter getTimestampConverter() {
        return timestampConverter;
    }

    public void setTimestampConverter(TimestampConverter timestampConverter) {
        this.timestampConverter = timestampConverter;
    }
}
