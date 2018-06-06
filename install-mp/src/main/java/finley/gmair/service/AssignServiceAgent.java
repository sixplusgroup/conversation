package finley.gmair.service;

import org.springframework.cloud.netflix.feign.FeignClient;

@FeignClient("install-agent")
public interface AssignServiceAgent {

}
