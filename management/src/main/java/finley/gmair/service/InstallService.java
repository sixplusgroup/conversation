package finley.gmair.service;

import finley.gmair.util.ResultData;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value = "install-agent")
public interface InstallService {
    /**
     * 调度人员创建安装任务，不带备注
     *
     * @param consumerConsignee
     * @param consumerPhone
     * @param consumerAddress
     * @param model
     * @return
     */
    @PostMapping("/install/assign/create")
    ResultData createAssign(@RequestParam("consumerConsignee") String consumerConsignee, @RequestParam("consumerPhone") String consumerPhone, @RequestParam("consumerAddress") String consumerAddress, @RequestParam(value = "model") String model, @RequestParam("source") String source);

    /**
     * 调度人员创建安装任务，带备注
     *
     * @param consumerConsignee
     * @param consumerPhone
     * @param consumerAddress
     * @param model
     * @param description
     * @return
     */
    @PostMapping("/install/assign/create")
    ResultData createAssign(@RequestParam("consumerConsignee") String consumerConsignee, @RequestParam("consumerPhone") String consumerPhone, @RequestParam("consumerAddress") String consumerAddress, @RequestParam(value = "model") String model, @RequestParam("source") String source, @RequestParam(value = "description", required = false) String description);

    //调度人员查看已有的安装任务
    @GetMapping("/install/assign/{assignId}/info")
    ResultData fetchAssign(@PathVariable("assignId") String assignId);

    @GetMapping("/install/assign/list")
    ResultData fetchAssign(@RequestParam(value = "status", required = false) String status, @RequestParam(value = "teamId", required = false) String teamId,@RequestParam(value = "search", required = false) String search);

    @GetMapping("/install/assign/list")
    ResultData fetchAssignByPage(@RequestParam(value = "status", required = false) String status, @RequestParam(value = "teamId", required = false) String teamId, @RequestParam(value = "start", required = false) int start, @RequestParam(value = "length", required = false) int length);

    //调度人员撤销安装任务
    @PostMapping("/install/assign/cancel")
    ResultData cancelAssign(@RequestParam(value = "assignId") String assignId, @RequestParam("message") String message);

    //调度人员将安装任务分配到安装团队
    @PostMapping("/install/assign/dispatch")
    ResultData dispatchAssign(@RequestParam("assignId") String assignId, @RequestParam("teamId") String teamId);

    //调度人员创建安装团队
    @PostMapping("/install/team/create")
    ResultData createTeam(@RequestParam("teamName") String teamName, @RequestParam("teamArea") String teamArea, @RequestParam(value = "teamDescription", required = false) String teamDescription);

    @GetMapping("/install/team/{teamId}/info")
    ResultData fetchTeam(@PathVariable("teamId") String teamId);

    @GetMapping("/install/team/list")
    ResultData fetchTeam();

    @GetMapping("/install/team/list")
    ResultData fetchTeam(@RequestParam(value = "start", required = false) int start, @RequestParam(value = "length", required = false) int length);

    @PostMapping("/install/member/watch/team")
    ResultData watch(@RequestParam("memberId") String memberId, @RequestParam("teamId") String teamId);

    @GetMapping("/install/assign/trace")
    ResultData trace(@RequestParam("assignId") String assignId);

    @GetMapping("/install/member/list")
    ResultData fetchTeamMember(@RequestParam("teamId") String teamId);

    @PostMapping("/install/member/create")
    ResultData createTeamMember(@RequestParam("teamId") String teamId, @RequestParam("memberPhone") String memberPhone, @RequestParam("memberName") String memberName, @RequestParam("memberRole") int memberRole);

    @PostMapping("/install/member/update")
    ResultData updatePhone(@RequestParam("memberPhone") String memberPhone, @RequestParam("memberId") String memberId);

    @GetMapping("/install/member/block")
    ResultData deleteMember(@RequestParam("memberId") String memberId);

    @GetMapping("/install/assign/snapshot")
    ResultData snapshot(@RequestParam("assignId") String assignId);

    @GetMapping("/install/team/block")
    ResultData deleteTeam(@RequestParam("teamId") String teamId);

    @PostMapping("/install/assign/submit")
    ResultData submitAssign(@RequestParam("assignId") String assignId, @RequestParam("qrcode") String qrcode, @RequestParam("picture") String picture, @RequestParam("wifi") Boolean wifi, @RequestParam("method") String method,@RequestParam("date") String date);

    @PostMapping("/install/assign/submit")
    ResultData submitAssign(@RequestParam("assignId") String assignId, @RequestParam("qrcode") String qrcode, @RequestParam("picture") String picture, @RequestParam("wifi") Boolean wifi, @RequestParam("method") String method, @RequestParam(value = "description", required = false) String description,@RequestParam("date") String date);

    @PostMapping("/install/assign/report")
    ResultData reportQueryByAssignId(@RequestParam("assignId") String assignId,@RequestParam("beginTime") String beginTime,@RequestParam("endTime") String endTime);

    @GetMapping("/install/assign/report")
    ResultData reportQueryByTeamId(@RequestParam("teamId") String teamId,@RequestParam("beginTime") String beginTime,@RequestParam("endTime") String endTime);

    @GetMapping("/install/assign/report")
    ResultData reportQueryByMemberId(@RequestParam("memberId") String memberId,@RequestParam("beginTime") String beginTime,@RequestParam("endTime") String endTime);

    @GetMapping("/install/assign/report")
    ResultData reportQueryByMemberTime(@RequestParam("beginTime") String beginTime,@RequestParam("endTime") String endTime);

    @PostMapping("/install/assign/init")
    ResultData initAssign(@RequestParam("assignId") String assignId, @RequestParam("qrcode") String qrcode);
}
