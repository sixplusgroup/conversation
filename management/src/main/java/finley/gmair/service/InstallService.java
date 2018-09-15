package finley.gmair.service;

import finley.gmair.util.ResultData;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value = "installation-agent")
public interface InstallService {

    @GetMapping("/installation/team/list")
    ResultData fetchTeamList();

    @PostMapping("/installation/team/create")
    ResultData createTeam(@RequestParam("teamName") String teamName, @RequestParam("teamArea") String teamArea, @RequestParam("teamDescription") String teamDescription);

    @GetMapping("/installation/member/list")
    ResultData fetchMemberList(@RequestParam("teamId") String teamId);

    @PostMapping("/installation/member/create")
    ResultData createMember(@RequestParam("teamId") String teamId, @RequestParam("memberPhone") String memberPhone, @RequestParam("memberName") String memberName, @RequestParam("memberRole") int memberRole);

    @PostMapping("/installation/reconnaissance/{reconnaissanceId}/process")
    ResultData reconnaissanceProcess(@PathVariable("reconnaissanceId") String reconnaissanceId,
                                     @RequestParam("orderId") String orderId,
                                     @RequestParam("setupMethod") String setupMethod,
                                     @RequestParam("description") String description,
                                     @RequestParam("reconDate") String reconDate,
                                     @RequestParam("reconStatus") int reconStatus
    );

    @PostMapping("/installation/assign/allocate")
    ResultData allocateInstallationAssign(@RequestParam("assignId") String assignId,
                                          @RequestParam("teamName") String teamName,
                                          @RequestParam("memberName") String memberName,
                                          @RequestParam("installDate") String installDate);

    @PostMapping("/installation/assign/create")
    ResultData createInstallationAssign(@RequestParam("qrcode") String qrcode,
                                        @RequestParam("consumerConsignee") String consumerConsignee,
                                        @RequestParam("consumerPhone") String consumerPhone,
                                        @RequestParam("consumerAddress") String consumerAddress);


    @GetMapping("/installation/assign/finishedinfo")
    ResultData finishedInfo(@RequestParam("assignId") String assignId);


    @GetMapping("/installation/assign/todolist")
    ResultData todoAssignList();

    @GetMapping("/installation/assign/assignedlist")
    ResultData assignedList();

    @GetMapping("/installation/assign/processinglist")
    ResultData processingAssignList();

    @GetMapping("/installation/assign/finishedlist")
    ResultData finishedList();

    @GetMapping("/installation/assign/closedlist")
    ResultData closedList();

    @GetMapping("/installation/assign/detail/list")
    ResultData detailList(@RequestParam("status") int status);

    @GetMapping("/installation/feedback/info")
    ResultData assignFeedback(@RequestParam("assignId") String assignId);

    @PostMapping("/installation/assign/delete")
    ResultData deleteAssign(@RequestParam("codeValue") String codeValue);
}
