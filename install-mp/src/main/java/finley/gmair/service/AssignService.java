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
    ResultData fetchAssign(@RequestParam("memberId") String memberId, @RequestParam(value = "status", required = true) Integer status, @RequestParam(value = "search", required = true) String search,@RequestParam("reverse") String reverse, @RequestParam(value = "sortType") String sortType);

    @GetMapping("/install/assign/tasks")
    ResultData fetchAssign(@RequestParam("memberId") String memberId, @RequestParam(value = "status", required = true) Integer status, @RequestParam(value = "search", required = true) String search,
                           @RequestParam("page") String page,@RequestParam("pageLength") String pageLength,@RequestParam("reverse") String reverse, @RequestParam(value = "sortType") String sortType);

    @PostMapping("/install/assign/assign")
    ResultData dispatchAssign(@RequestParam("assignId") String assignId, @RequestParam("memberId") String memberId);

    @GetMapping("/install/assign/overview")
    ResultData fetchOwnAssign(@RequestParam("memberId") String memberId);

    @GetMapping("/install/assign/overview")
    ResultData fetchOwnAssign(@RequestParam("memberId") String memberId, @RequestParam(value = "status", required = true) Integer status, @RequestParam(value = "search", required = true) String search,  @RequestParam(value = "sortType", required = true) String sortType);

    @PostMapping("/install/assign/recall")
    ResultData recallAssign(@RequestParam("assignId") String assignId, @RequestParam("message") String message);

    @PostMapping("/install/assign/cancel")
    ResultData cancelAssign(@RequestParam("assignId") String assignId, @RequestParam("message") String message);

    @GetMapping("/install/assign/trace")
    ResultData traceAssign(@RequestParam("assignId") String assignId);

    @PostMapping("/install/assign/init")
    ResultData initAssign(@RequestParam("assignId") String assignId, @RequestParam("qrcode") String qrcode);

    @PostMapping("/install/assign/submit")
    ResultData submitAssign(@RequestParam("assignId") String assignId, @RequestParam("qrcode") String qrcode, @RequestParam("picture") String picture, @RequestParam("wifi") Boolean wifi, @RequestParam("method") String method,@RequestParam("date") String date);

    @PostMapping("/install/assign/submit")
    ResultData submitAssign(@RequestParam("assignId") String assignId, @RequestParam("qrcode") String qrcode, @RequestParam("picture") String picture, @RequestParam("wifi") Boolean wifi, @RequestParam("method") String method, @RequestParam(value = "description", required = false) String description,@RequestParam("date") String date);

    @GetMapping("/install/assign/snapshot")
    ResultData snapshotAssign(@RequestParam("assignId") String assignId);

    @PostMapping("/install/assign/eval")
    ResultData evalAssign(@RequestParam("assignId") String assignId, @RequestParam("code") String code);

    @PostMapping("/install/assign/receive")
    ResultData receive(@RequestParam("assignId")String assignId);

    @GetMapping("/install/assign/assignTypeInfo/one")
    ResultData queryAssignTypeInfoByType(@RequestParam("assignType") String assignType);
}
