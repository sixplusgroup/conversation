package finley.gmair.service;

import finley.gmair.util.ResultData;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value = "install-agent")
public interface InstallService {
    //调度人员创建安装任务
    @PostMapping("/install/assign/create")
    ResultData createAssign(@RequestParam("consumerConsignee") String consumerConsignee, @RequestParam("consumerPhone") String consumerPhone, @RequestParam("consumerAddress") String consumerAddress, @RequestParam(value = "model") String model);

    //调度人员查看已有的安装任务
    @GetMapping("/install/assign/list")
    ResultData fetchAssign(@RequestParam(value = "status", required = false) String status, @RequestParam(value = "teamId", required = false) String teamId);

    //调度人员查看已有的安装任务列表
    @GetMapping("/install/assign/list")
    ResultData fetchAssignByPage(@RequestParam(value = "status", required = false) String status, @RequestParam(value = "teamId", required = false) String teamId, @RequestParam(value = "start", required = false) int start, @RequestParam(value = "length", required = false) int length);

    @PostMapping("/install/assign/cancel")
    ResultData cancelAssign(@RequestParam(value = "assignId") String assignId);
}
