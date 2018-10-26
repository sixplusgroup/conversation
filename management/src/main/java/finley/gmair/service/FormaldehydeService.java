package finley.gmair.service;

import finley.gmair.util.ResultData;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient("formaldehyde-agent")
public interface FormaldehydeService {
    @PostMapping("/formaldehyde/case/profile/create")
    ResultData createCaseProfile(@RequestParam("caseHolder") String caseHolder,
                                 @RequestParam("equipmentId") String equipmentId,
                                 @RequestParam("checkDuration") String checkDuration,
                                 @RequestParam("checkDate") String checkDate,
                                 @RequestParam("caseCityId") String caseCityId,
                                 @RequestParam("caseCityName") String caseCityName,
                                 @RequestParam("caseLocation") String caseLocation,
                                 @RequestParam("checkTrace") String checkTrace,
                                 @RequestParam("videoId") String videoId);

    @GetMapping("/formaldehyde/case/location/fetch")
    ResultData getCaseLocationList();

    @GetMapping("/formaldehyde/case/profile/fetch")
    ResultData fetchCaseProfile(
            @RequestParam("caseId") String caseId,
            @RequestParam("caseHolder") String caseHolder,
            @RequestParam("equipmentId") String equipmentId,
            @RequestParam("checkDate") String checkDate,
            @RequestParam("caseCityId") String caseCityId,
            @RequestParam("caseStatus") String caseStatus,
            @RequestParam("videoId") String videoId) ;


    @GetMapping("/formaldehyde/check/equipment/fetch")
    ResultData getEquipmentById(@RequestParam("equipmentId") String equipmentId);
}
