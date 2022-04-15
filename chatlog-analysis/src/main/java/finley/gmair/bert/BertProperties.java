package finley.gmair.bert;

import lombok.Data;
import lombok.ToString;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ToString
@ConfigurationProperties(prefix = "python.bert")
public class BertProperties {
    String configFile;
    String vocabFile;
    String checkpointDir;
    boolean doTrain = false;
    boolean doEval = false;
    boolean doPredict = true;

    public void setCheckpointDir(String checkpointDir) {
        this.checkpointDir = "'" + checkpointDir + "'";
    }

    public void setConfigFile(String configFile) {
        this.configFile = "'" + configFile + "'";
    }

    public void setVocabFile(String vocabFile) {
        this.vocabFile = "'" + vocabFile + "'";
    }
}
