package finley.gmair.bert;

import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Getter
@Component
@ConfigurationProperties(prefix = "python.bert")
public class BertProperties {
    String configFile;
    String vocabFile;
    String checkpointDir;
    boolean doTrain = false;
    boolean doEval = false;
    boolean doPredict = true;
}
