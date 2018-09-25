package finley.gmair.service.impl;

import finley.gmair.dao.RepositoryDao;
import finley.gmair.model.drift.DriftRepository;
import finley.gmair.service.RepositoryService;
import finley.gmair.util.ResponseCode;
import finley.gmair.util.ResultData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class RepositoryServiceImpl implements RepositoryService {

    @Autowired
    private RepositoryDao repositoryDao;

    @Override
    public ResultData fetchRepository(Map<String, Object> condition) {
        ResultData result = new ResultData();
        ResultData response = repositoryDao.queryRepository(condition);
        switch (response.getResponseCode()) {
            case RESPONSE_NULL:
                result.setResponseCode(ResponseCode.RESPONSE_NULL);
                result.setDescription("No repository found from database");
                break;
            case RESPONSE_ERROR:
                result.setResponseCode(ResponseCode.RESPONSE_ERROR);
                result.setDescription("Fail to retrieve repository");
                break;
            case RESPONSE_OK:
                result.setResponseCode(ResponseCode.RESPONSE_OK);
                result.setData(response.getData());
                break;
        }
        return result;
    }

    @Override
    public ResultData createRepository(DriftRepository repository) {
        ResultData result = new ResultData();
        ResultData response = repositoryDao.insertRepository(repository);
        switch (response.getResponseCode()) {
            case RESPONSE_ERROR:
                result.setResponseCode(ResponseCode.RESPONSE_ERROR);
                result.setDescription("Fail to insert repository to database");
                break;
            case RESPONSE_OK:
                result.setResponseCode(ResponseCode.RESPONSE_OK);
                result.setData(response.getData());
                break;
            default:
                break;
        }
        return result;
    }

    @Override
    public ResultData modifyRepository(Map<String, Object> condition) {
        ResultData result = new ResultData();
        ResultData response = repositoryDao.updateRepository(condition);
        if (response.getResponseCode() == ResponseCode.RESPONSE_ERROR) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("Fail to update repository");
        }
        if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
            result.setResponseCode(ResponseCode.RESPONSE_OK);
            result.setDescription("Succeed to update repository");
        }
        return result;
    }
}
