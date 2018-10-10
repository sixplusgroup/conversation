package finley.gmair.controller;

import finley.gmair.service.AssembleService;
import finley.gmair.util.ResultData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
@RequestMapping("/management/assemble")
public class AssembleController {
    @Autowired
    private AssembleService assembleService;

    @PostMapping("/barcode/create")
    public ResultData createBatch(int number){
        return assembleService.createBatch(number);
    }

    @GetMapping("/snapshot/fetch")
    public ResultData fetchSnapshot(String startTime,String endTime,String codeValue, String snapshotId){
        return assembleService.fetchSnapshot(startTime,endTime,codeValue,snapshotId);
    }

    @PostMapping("/checkrecord/create")
    public ResultData createCheckRecord(String snapshotId, boolean recordStatus){
        return assembleService.createCheckRecord(snapshotId,recordStatus);
    }

    @GetMapping("/checkrecord/fetch/bystatus")
    public ResultData fetchCheckRecordByStatus(boolean recordStatus){
        return assembleService.fetchCheckRecordByStatus(recordStatus);
    }
}
