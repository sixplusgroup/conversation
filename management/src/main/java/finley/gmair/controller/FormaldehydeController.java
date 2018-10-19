package finley.gmair.controller;

import finley.gmair.service.FormaldehydeService;
import finley.gmair.util.ResultData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/fomaldehyde")
public class FormaldehydeController {
    @Autowired
    private FormaldehydeService formaldehydeService;

    @PostMapping("/case/create")
    public ResultData createCase(String caseHolder, String equipmentId, String checkDuration, String checkDate, String caseCityId, String caseCityName, String caseLocation, String checkTrace, String videoId) {
        return formaldehydeService.createCaseProfile(caseHolder, equipmentId, checkDuration, checkDate, caseCityId, caseCityName, caseLocation, checkTrace, videoId);
    }
}
