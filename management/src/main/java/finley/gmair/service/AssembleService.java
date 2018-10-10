package finley.gmair.service;

import finley.gmair.util.ResultData;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient("assemble-agent")
public interface AssembleService {


    @PostMapping("/assemble/barcode/batch/create")
    ResultData createBatch(@RequestParam("number") int number);

    @GetMapping("/assemble/snapshot/fetch")
    ResultData fetchSnapshot(@RequestParam("startTime") String startTime,
                             @RequestParam("endTime") String endTime,
                             @RequestParam("codeValue") String codeValue,
                             @RequestParam("snapshotId") String snapshotId);

    @PostMapping("/assemble/checkrecord/create")
    ResultData createCheckRecord(@RequestParam("snapshotId") String snapshotId,
                                 @RequestParam("recordStatus") boolean recordStatus);

    @GetMapping("/assemble/checkrecord/fetch/bystatus")
    ResultData fetchCheckRecordByStatus(@RequestParam("recordStatus") boolean recordStatus);

}
