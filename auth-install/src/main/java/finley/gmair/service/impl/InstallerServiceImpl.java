package finley.gmair.service.impl;

import com.alibaba.fastjson.JSON;
import finley.gmair.dao.InstallerDao;
import finley.gmair.service.InstallerService;
import finley.gmair.util.ResponseCode;
import finley.gmair.util.ResultData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class InstallerServiceImpl implements InstallerService {

    @Autowired
    private InstallerDao installerDao;

    @Override
    public ResultData queryInstaller(Map<String, Object> condition) {
        ResultData result = new ResultData();
        ResultData response = installerDao.query(condition);
        if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
            result.setResponseCode(ResponseCode.RESPONSE_OK);
            result.setData(response.getData());
        }
        if (response.getResponseCode() == ResponseCode.RESPONSE_NULL) {
            result.setResponseCode(ResponseCode.RESPONSE_NULL);
            result.setDescription(new StringBuffer("No worker found with the given condition: ").append(JSON.toJSONString(condition)).toString());
        }
        if (response.getResponseCode() == ResponseCode.RESPONSE_ERROR) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("Fail to fetch any worker");
        }
        return result;
    }
}
