package finley.gmair.service;

import finley.gmair.model.installation.Member;
import finley.gmair.util.ResultData;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.Map;

public interface InstallerService {
    ResultData fetchInstaller(Map<String, Object> condition);

    ResultData reviseInstaller(Member member);
}
