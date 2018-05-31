package finley.gmair.service;

import org.springframework.cloud.netflix.feign.FeignClient;

@FeignClient("express-agent")
public interface ExpressFeignService {

}
