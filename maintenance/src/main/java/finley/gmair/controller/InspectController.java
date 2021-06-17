package finley.gmair.controller;

import finley.gmair.form.installation.AssignForm;
import finley.gmair.service.InstallService;
import finley.gmair.util.ResultData;
import org.apache.commons.lang.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 工人上门检修的交互接口
 *
 * @author lycheeshell
 * @date 2021/1/20 16:41
 */
@RestController
@RequestMapping("/maintenance/inspect")
public class InspectController {

    @Resource
    private InstallService installService;

    /**
     * 创建设备上门检修
     *
     * @param form 检修预约信息
     * @return 创建结果
     */
    @PostMapping(value = "/createInspectTask")
    public ResultData createInspectTask(AssignForm form) {
        if (StringUtils.isEmpty(form.getConsumerConsignee()) || StringUtils.isEmpty(form.getConsumerPhone())
                || StringUtils.isEmpty(form.getConsumerAddress())) {
            return ResultData.error("请输入所有的安装任务用户相关的信息");
        }
        String consumerConsignee = form.getConsumerConsignee();
        String consumerPhone = form.getConsumerPhone();
        String consumerAddress = form.getConsumerAddress();
        String model = form.getModel();
        String description = form.getDescription();
        String source = "客服反馈";
        String type = "检修";
        return installService.create(consumerConsignee, consumerPhone, consumerAddress,
                model, source, description, type);
    }
}
