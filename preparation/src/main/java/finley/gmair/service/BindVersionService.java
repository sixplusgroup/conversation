package finley.gmair.service;

import finley.gmair.util.ResultData;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

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

}
