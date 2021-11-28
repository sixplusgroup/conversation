package finley.gmair.service;

import finley.gmair.util.ResultData;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @ClassName: QRCodeService
 * @Description: TODO
 * @Author fan
 * @Date 2019/4/18 4:13 PM
 */
@FeignClient("machine-agent")
public interface QRCodeService {

    @PostMapping("/machine/qrcode/probe/byurl")
    ResultData decodeURL(@RequestParam("codeUrl") String codeUrl);

    @GetMapping("/machine/filter/info/model/name")
    ResultData queryMachineModelName();

}
