package finley.gmair.service;
import finley.gmair.util.ResultData;
import org.springframework.cloud.netflix.feign.FeignClient;
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
}
