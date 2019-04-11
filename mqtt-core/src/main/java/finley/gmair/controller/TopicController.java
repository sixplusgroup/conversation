package finley.gmair.controller;

import finley.gmair.form.mqtt.TopicForm;
import finley.gmair.model.mqtt.Topic;
import finley.gmair.service.TopicService;
import finley.gmair.util.ResponseCode;
import finley.gmair.util.ResultData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/mqtt/topic")
public class TopicController {

    @Autowired
    private TopicService topicService;

    @PostMapping(value = "/create")
    public ResultData createTopic(TopicForm form) {
        ResultData result = new ResultData();
        if (StringUtils.isEmpty(form.getTopicDetail()) || StringUtils.isEmpty(form.getTopicDescription())) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("Please make sure you fill all the required fields");
            return result;
        }
        String topicDetail = form.getTopicDetail().trim();
        String topicDescription = form.getTopicDescription().trim();
        Topic topic = new Topic(topicDetail, topicDescription);
        ResultData response = topicService.create(topic);
        if (response.getResponseCode() != ResponseCode.RESPONSE_OK) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("Create topic unsuccessfully");
            return result;
        }
        result.setResponseCode(ResponseCode.RESPONSE_OK);
        result.setData(response.getData());
        return result;
    }

    @PostMapping ("/query")
    public ResultData getTopic() {
        ResultData result = new ResultData();
        Map<String, Object> condition = new HashMap<>();
        condition.put("blockFlag", false);
        ResultData response = topicService.fetch(condition);
        if (response.getResponseCode() == ResponseCode.RESPONSE_NULL) {
            result.setResponseCode(ResponseCode.RESPONSE_NULL);
            result.setDescription("No topic got");
        }
        if (response.getResponseCode() == ResponseCode.RESPONSE_ERROR) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("Fail to get topic");
        }
        if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
            result.setResponseCode(ResponseCode.RESPONSE_OK);
            result.setData(response.getData());
        }
        return result;
    }

}
