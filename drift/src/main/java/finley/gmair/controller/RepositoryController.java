package finley.gmair.controller;

import finley.gmair.form.drift.RepositoryForm;
import finley.gmair.model.drift.DriftRepository;
import finley.gmair.service.RepositoryService;
import finley.gmair.util.ResponseCode;
import finley.gmair.util.ResultData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/assemble/repository")
public class RepositoryController {

    @Autowired
    private RepositoryService repositoryService;

    @PostMapping(value = "/create")
    public ResultData createRepository(RepositoryForm form) {
        ResultData result = new ResultData();
        //judge the parameter complete or not
        if (StringUtils.isEmpty(form.getGoodsId()) || StringUtils.isEmpty(form.getPoolSize())) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("please make sure you fill all the required fields");
            return result;
        }

        //build the repository entity
        DriftRepository repository = new DriftRepository(form.getGoodsId(), form.getPoolSize());
        ResultData response = repositoryService.createRepository(repository);
        if (response.getResponseCode() == ResponseCode.RESPONSE_ERROR) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("Fail to store repository to database");
        }
        if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
            result.setResponseCode(ResponseCode.RESPONSE_OK);
            result.setData(response.getData());
        }
        return result;
    }

    @GetMapping(value = "list")
    public ResultData getRepository() {
        ResultData result = new ResultData();
        Map<String, Object> condition = new HashMap<>();
        condition.put("blockFlag", false);
        ResultData response = repositoryService.fetchRepository(condition);
        switch (response.getResponseCode()) {
            case RESPONSE_NULL:
                result.setResponseCode(ResponseCode.RESPONSE_NULL);
                result.setDescription("No repository found");
                break;
            case RESPONSE_ERROR:
                result.setResponseCode(ResponseCode.RESPONSE_ERROR);
                result.setDescription("Query error");
                break;
            case RESPONSE_OK:
                result.setResponseCode(ResponseCode.RESPONSE_OK);
                result.setData(response.getData());
                break;
        }
        return result;
    }
}
