package finley.gmair.service;

import finley.gmair.util.ResultData;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient("formaldehyde-agent")
public interface FormaldehydeService {
    @PostMapping("/case/profile/create")
    ResultData createCaseProfile(@RequestParam("caseHolder") String caseHolder,
                                 @RequestParam("equipmentId") String equipmentId,
                                 @RequestParam("checkDuration") String checkDuration,
                                 @RequestParam("checkDate") String checkDate,
                                 @RequestParam("caseCityId") String caseCityId,
                                 @RequestParam("caseCityName") String caseCityName,
                                 @RequestParam("caseLocation") String caseLocation,
                                 @RequestParam("checkTrace") String checkTrace,
                                 @RequestParam("videoId") String videoId);
}
