package finley.gmair.controller;

import finley.gmair.netty.GMRepository;
import finley.gmair.util.ResponseCode;
import finley.gmair.util.ResultData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/core/repo")
public class RepositoryController {

    @Autowired
    private GMRepository repository;

    @GetMapping("/available")
    public ResultData list() {
        ResultData result = new ResultData();
        ResultData response = repository.list();
        if (response.getResponseCode() == ResponseCode.RESPONSE_NULL) {
            result.setResponseCode(ResponseCode.RESPONSE_NULL);
        }
        if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
            result.setData(response.getData());
        }
        return result;
    }

    @GetMapping("/{machineId}/online")
    public ResultData isOnline(@PathVariable("machineId") String machineId) {
        return repository.isOnline(machineId);
    }
}
