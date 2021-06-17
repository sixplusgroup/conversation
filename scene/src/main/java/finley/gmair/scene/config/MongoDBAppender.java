package finley.gmair.scene.config;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.UnsynchronizedAppenderBase;
import com.mongodb.BasicDBObject;
import org.springframework.data.mongodb.core.MongoTemplate;

/**
 * @author : Lyy
 * @create : 2021-01-15 16:40
 **/
public class MongoDBAppender extends
        UnsynchronizedAppenderBase<ILoggingEvent> {

    @Override
    protected void append(ILoggingEvent eventObject) {
        MongoTemplate mongoTemplate = ApplicationContextProvider.getBean(MongoTemplate.class);
        if (mongoTemplate != null) {
            final BasicDBObject doc = new BasicDBObject();
            doc.append("level", eventObject.getLevel().toString());
            doc.append("logger", eventObject.getLoggerName());
            doc.append("thread", eventObject.getThreadName());
            doc.append("message", eventObject.getFormattedMessage());
            mongoTemplate.insert(doc, "log");
        }
    }

}