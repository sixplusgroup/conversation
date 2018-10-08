package finley.gmair.service;

import finley.gmair.util.ResultData;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "machine-agent")
public interface BindVersionService {
    @RequestMapping(method = RequestMethod.POST, value = "/machine/board/record/single")
    ResultData recordSingleBoardVersion(@RequestParam("machineId") String machineId,
                                        @RequestParam("version") int version);

    @RequestMapping(method = RequestMethod.POST, value = "/machine/qrcode/prebind")
    ResultData preBind(@RequestParam("machineId") String machineId,
                       @RequestParam("codeValue") String codeValue);

    @RequestMapping(method = RequestMethod.GET, value = "/machine/qrcode/check/existqrcode")
    ResultData checkQRcodeExist(@RequestParam("codeValue") String codeValue);

    @RequestMapping(method = RequestMethod.GET, value = "/machine/qrcode/findbyqrcode")
    ResultData findMachineIdByCodeValue(@RequestParam("codeValue") String codeValue);

    @RequestMapping(method = RequestMethod.GET, value = "/machine/qrcode/check/existmachineid")
    ResultData checkMachineIdExist(@RequestParam("machineId") String machineId);

    @GetMapping(value = "/machine/qrcode/prebind/list/now")
    ResultData findPrebind();

    @GetMapping(value = "/machine/qrcode/prebind/list")
    ResultData prebindList(@RequestParam("param") String param);

    @PostMapping(value = "/machine/board/bind/batch")
    ResultData bindBatchVersion(@RequestParam("bindList") String bindList);

    @PostMapping(value = "/machine/board/bind/delete")
    ResultData deletePreBind(@RequestParam("bindId") String bindId);

}
