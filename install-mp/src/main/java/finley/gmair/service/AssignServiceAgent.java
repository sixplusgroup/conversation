package finley.gmair.service;

import finley.gmair.util.ResultData;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient("install-agent")
public interface AssignServiceAgent {

    @PostMapping("/installation/assign/postpone")
    ResultData postpone(@RequestParam("assignId") String assignId, @RequestParam("date") String date);

    @PostMapping("/installation/assign/cancel")
    ResultData cancel(@RequestParam("assignId") String assignId, @RequestParam("description") String description);
}
