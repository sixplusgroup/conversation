package finley.gmair.controller;


import finley.gmair.service.FormaldehydeService;
import finley.gmair.util.ResultData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/map/formaldehyde")
public class FormaldehydeController {


    @Autowired
    private FormaldehydeService formaldehydeService;

    @GetMapping("/list/fetch")
    public ResultData getMapList(){
        return formaldehydeService.getCaseLocationList();
    }

    @GetMapping("/case/profile/fetch")
    public ResultData getCaseProfile(String caseId){
        return formaldehydeService.fetchCaseProfile(caseId,null,null,null,null,null,null);
    }

    @GetMapping("/check/equipment/fetch")
    public ResultData getCheckEquipment(String equipmentId){
        return formaldehydeService.getEquipmentById(equipmentId);
    }
}
