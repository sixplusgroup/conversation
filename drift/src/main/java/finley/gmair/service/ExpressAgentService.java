package finley.gmair.service;

import com.alibaba.fastjson.JSONObject;
import finley.gmair.util.ResultData;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@FeignClient("express-agent")
public interface ExpressAgentService {
    @PostMapping("/express/subscribe")
    ResultData subscribe(@RequestParam("expressCompany") String expressCompany,
                         @RequestParam("expressNo") String expressNo);

    @GetMapping("/express/query")
    ResultData getExpress(@RequestParam("expressNo") String expressNo,
                          @RequestParam("expressCompany") String expressCompany);

    @PostMapping("/express/receive")
    ResultData receive(@RequestParam("param") String param);
}
