package finley.gmair.service;

import finley.gmair.util.ResultData;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value = "install-agent")
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
}
