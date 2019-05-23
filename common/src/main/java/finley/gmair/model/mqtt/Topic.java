package finley.gmair.model.mqtt;

import finley.gmair.model.Entity;

public class Topic extends Entity {
    private String topicId;
    private String topicDetail;
    private String topicDescription;

    public Topic() {
        super();
    }

    public Topic(String topicDetail, String topicDescription) {
        this();
        this.topicDetail = topicDetail;
        this.topicDescription = topicDescription;
    }

    public String getTopicId() {
        return topicId;
    }

    public void setTopicId(String topicId) {
        this.topicId = topicId;
    }

    public String getTopicDetail() {
        return topicDetail;
    }

    public void setTopicDetail(String topicDetail) {
        this.topicDetail = topicDetail;
    }

    public String getTopicDescription() {
        return topicDescription;
    }

    public void setTopicDescription(String topicDescription) {
        this.topicDescription = topicDescription;
    }
}
