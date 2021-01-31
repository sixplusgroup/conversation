package finley.gmair.controller;

import finley.gmair.exception.MqttBusinessException;
import finley.gmair.service.TopicService;
import finley.gmair.util.ResultData;
import finley.gmair.util.VerifyUtil;
import org.apache.commons.lang.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 消息主题处理
 *
 * @author lycheeshell
 * @date 2020/12/15 16:27
 */
@RestController
@RequestMapping("/mqtt/topic")
public class TopicController {

    @Resource
    private TopicService topicService;

    /**
     * 新增主题
     *
     * @param topicDetail      主题格式
     * @param topicDescription 主题描述
     * @return 新增行数
     * @throws MqttBusinessException 异常
     */
    @PostMapping(value = "/create")
    public ResultData createTopic(String topicDetail, String topicDescription) throws MqttBusinessException {
        VerifyUtil.verify(StringUtils.isNotEmpty(topicDetail), "主题格式为空");
        VerifyUtil.verify(StringUtils.isNotEmpty(topicDescription), "主题描述说明为空");

        return ResultData.ok(topicService.addTopic(topicDetail, topicDescription));
    }

    /**
     * 删除主题
     *
     * @param topicId 主题id
     * @return 删除行数
     * @throws MqttBusinessException 异常
     */
    @PostMapping(value = "/delete")
    public ResultData deleteTopic(String topicId) throws MqttBusinessException {
        VerifyUtil.verify(StringUtils.isNotEmpty(topicId), "主题id为空");

        return ResultData.ok(topicService.deleteTopic(topicId));
    }

    /**
     * 查询主题列表
     *
     * @return 主题列表
     */
    @GetMapping ("/query")
    public ResultData getTopic() {
        return ResultData.ok(topicService.queryTopics(null, null, null));
    }

    /**
     * 模糊查询，查询主题列表
     *
     * @param topicDetail 主题格式
     * @return 主题列表
     */
    @GetMapping ("/getTopicByDetail")
    public ResultData getTopicByDetail(String topicDetail) {
        if (StringUtils.isEmpty(topicDetail.trim())) {
            topicDetail = "";
        }
        return ResultData.ok(topicService.queryTopicsByDetail(topicDetail.trim()));
    }


}
