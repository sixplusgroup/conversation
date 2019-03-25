package finley.gmair.model.mqtt;

import finley.gmair.model.Entity;

public class ApiTopic extends Entity {
    private String boundId;
    private String apiName;
    private String apiUrl;
    private String apiTopic;
    private String apiDescription;

    public ApiTopic() {
        super();
    }

    public ApiTopic(String apiName, String apiUrl, String apiTopic, String apiDescription) {
        this();
        this.apiName = apiName;
        this.apiUrl = apiUrl;
        this.apiTopic = apiTopic;
        this.apiDescription = apiDescription;
    }

    public String getBoundId() {
        return boundId;
    }

    public void setBoundId(String boundId) {
        this.boundId = boundId;
    }

    public String getApiName() {
        return apiName;
    }

    public void setApiName(String apiName) {
        this.apiName = apiName;
    }

    public String getApiUrl() {
        return apiUrl;
    }

    public void setApiUrl(String apiUrl) {
        this.apiUrl = apiUrl;
    }

    public String getApiTopic() {
        return apiTopic;
    }

    public void setApiTopic(String apiTopic) {
        this.apiTopic = apiTopic;
    }

    public String getApiDescription() {
        return apiDescription;
    }

    public void setApiDescription(String apiDescription) {
        this.apiDescription = apiDescription;
    }
}
