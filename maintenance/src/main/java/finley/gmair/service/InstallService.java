package finley.gmair.service;

import finley.gmair.util.ResultData;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * 调用installdesk服务
 *
 * @author lycheeshell
 * @date 2021/1/20 16:01
 */
@FeignClient("install-agent")
public interface InstallService {

    /**
     * 调度人员创建安装任务，带备注
     *
     * @param consumerConsignee 客户姓名
     * @param consumerPhone 客户电话
     * @param consumerAddress 客户地址
     * @param model 设备型号
     * @param source 来源
     * @param description 备注
     * @param type 安装类型
     * @return 创建结果
     */
    @PostMapping("/install/assign/create")
    ResultData create(@RequestParam("consumerConsignee") String consumerConsignee,
                      @RequestParam("consumerPhone") String consumerPhone,
                      @RequestParam("consumerAddress") String consumerAddress,
                      @RequestParam(value = "model") String model,
                      @RequestParam("source") String source,
                      @RequestParam(value = "description", required = false) String description,
                      @RequestParam("type") String type);

}
