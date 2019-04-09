package finley.gmair.controller;

import finley.gmair.service.AssignService;
import finley.gmair.util.ResponseCode;
import finley.gmair.util.ResultData;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @ClassName: AssignController
 * @Description: TODO
 * @Author fan
 * @Date 2019/4/9 4:21 PM
 */
@RestController
@RequestMapping("/install-mp/assign")
public class AssignController {
    private Logger logger = LoggerFactory.getLogger(AssignController.class);

    @Autowired
    private AssignService assignService;

    /**
     * 安装负责人查看安装任务
     *
     * @param memberId
     * @return
     */
    @GetMapping("/tasks")
    public ResultData assigns(String memberId, Integer status) {
        ResultData result = new ResultData();
        if (StringUtils.isEmpty(memberId)) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("请提供安装负责人的信息");
            return result;
        }
        if (status == null) {
            result = assignService.fetchAssign(memberId);
        } else {
            result = assignService.fetchAssign(memberId, status);
        }
        return result;
    }

}