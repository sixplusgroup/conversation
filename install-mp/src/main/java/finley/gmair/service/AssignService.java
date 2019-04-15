package finley.gmair.service;

import finley.gmair.util.ResultData;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient("install-agent")
public interface AssignService {

    @GetMapping("/install/assign/tasks")
    ResultData fetchAssign(@RequestParam("memberId") String memberId);

    @GetMapping("/install/assign/tasks")
    ResultData fetchAssign(@RequestParam("memberId") String memberId, @RequestParam(value = "status", required = false) Integer status);

    @PostMapping("/install/assign/assign")
    ResultData dispatchAssign(@RequestParam("assignId") String assignId, @RequestParam("memberId") String memberId);

    @GetMapping("/install/assign/overview")
    ResultData fetchOwnAssign(@RequestParam("memberId") String memberId);

    @GetMapping("/install/assign/overview")
    ResultData fetchOwnAssign(@RequestParam("memberId") String memberId, @RequestParam(value = "status", required = false) Integer status);

    @PostMapping("/install/assign/recall")
    ResultData recallAssign(@RequestParam("assignId") String assignId, @RequestParam("message") String message);

    @PostMapping("/install/assign/cancel")
    ResultData cancelAssign(@RequestParam("assignId") String assignId, @RequestParam("message") String message);
}
