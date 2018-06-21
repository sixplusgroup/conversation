package finley.gmair.service.impl;

import finley.gmair.dao.BoardVersionDao;
import finley.gmair.model.machine.BoardVersion;
import finley.gmair.service.BoardVersionService;
import finley.gmair.util.ResponseCode;
import finley.gmair.util.ResultData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class BoardVersionServiceImpl implements BoardVersionService {

    @Autowired
    private BoardVersionDao boardVersionDao;

    @Override
    public ResultData createBoardVersion(BoardVersion version) {
        ResultData result = new ResultData();
        ResultData response = boardVersionDao.insertBoardVersion(version);
        if (response.getResponseCode() == ResponseCode.RESPONSE_ERROR) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription(new StringBuffer("Fail to insert board version into database.").toString());
            return result;
        }
        result.setData(response.getData());
        return result;
    }

    @Override
    public ResultData fetchBoardVersion(Map<String, Object> condition) {
        ResultData result = new ResultData();
        ResultData response = boardVersionDao.queryBoardVersion(condition);
        if (response.getResponseCode() == ResponseCode.RESPONSE_ERROR) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("Fail to fetch board version information from database.");
            return result;
        }
        if (response.getResponseCode() == ResponseCode.RESPONSE_NULL) {
            result.setResponseCode(ResponseCode.RESPONSE_NULL);
            result.setDescription("No board version information found.");
            return result;
        }
        result.setData(response.getData());
        return result;
    }
}
