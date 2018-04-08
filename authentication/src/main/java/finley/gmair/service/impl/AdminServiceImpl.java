package finley.gmair.service.impl;

import com.alibaba.fastjson.JSON;
import finley.gmair.dao.AdminDao;
import finley.gmair.model.admin.Admin;
import finley.gmair.service.AdminService;
import finley.gmair.util.ResponseCode;
import finley.gmair.util.ResultData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.ResponseErrorHandler;

import java.util.Map;

@Service
public class AdminServiceImpl implements AdminService {
    @Autowired
    private AdminDao adminDao;

    @Override
    public ResultData fetchAdmin(Map<String, Object> condition) {
        ResultData result = new ResultData();
        ResultData response = adminDao.query(condition);
        if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
            result.setResponseCode(ResponseCode.RESPONSE_OK);
            result.setData(response.getData());
        }
        if (response.getResponseCode() == ResponseCode.RESPONSE_NULL) {
            result.setResponseCode(ResponseCode.RESPONSE_NULL);
            result.setDescription(new StringBuffer("No admin info found with: ").append(JSON.toJSONString(condition)).toString());
        }
        if (response.getResponseCode() == ResponseCode.RESPONSE_ERROR) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("Fail to fetch admin information from database");
        }
        return result;
    }

    @Override
    public ResultData createAdmin(Admin admin) {
        ResultData result = new ResultData();
        ResultData response = adminDao.insert(admin);
        if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
            result.setResponseCode(ResponseCode.RESPONSE_OK);
            result.setData(response.getData());
        }
        if (response.getResponseCode() == ResponseCode.RESPONSE_ERROR) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription(new StringBuffer("Fail to create admin with: ").append(JSON.toJSONString(admin)).toString());
        }
        return result;
    }

    @Override
    public ResultData reviseAdmin(Admin admin) {
        ResultData result = new ResultData();
        ResultData response = adminDao.update(admin);
        if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
            result.setResponseCode(ResponseCode.RESPONSE_OK);
            result.setData(response.getData());
        }
        if (response.getResponseCode() == ResponseCode.RESPONSE_ERROR) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription(new StringBuffer("Fail to revise admin with admin id: ").append(admin.getAdminId()).append(" to: ").append(JSON.toJSONString(admin)).toString());
        }
        return result;
    }
}
