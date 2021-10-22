package finley.gmair.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import finley.gmair.dao.CorpNotificationDao;
import finley.gmair.model.openplatform.CorpNotification;
import finley.gmair.service.MachineNotifyService;
import finley.gmair.util.HttpDeal;
import finley.gmair.util.ResponseCode;
import finley.gmair.util.ResultData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @ClassName: MachineNotifyServiceImpl
 * @Description: TODO
 * @Author fan
 * @Date 2019/11/13 1:18 PM
 */
@Service
public class MachineNotifyServiceImpl implements MachineNotifyService {
    private Logger logger = LoggerFactory.getLogger(MachineNotifyServiceImpl.class);

    @Autowired
    private CorpNotificationDao corpNotificationDao;

    @Override
    public ResultData notify(String corpId, String qrcode) {
        ResultData result = new ResultData();
        Map<String, Object> condition = new HashMap<>();
        condition.put("corpId", corpId);
        condition.put("blockFlag", false);
        ResultData response = corpNotificationDao.query(condition);
        if (response.getResponseCode() == ResponseCode.RESPONSE_ERROR) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription(response.getDescription());
            return result;
        }
        if (response.getResponseCode() == ResponseCode.RESPONSE_NULL) {
            result.setResponseCode(ResponseCode.RESPONSE_NULL);
            result.setDescription("Cannot find any notification configuration for corp: " + corpId);
            return result;
        }
        CorpNotification notification = ((List<CorpNotification>) response.getData()).get(0);
        String url = notification.getUrl();
        String header = notification.getHeader();
        Map<String, String> map = new HashMap<>();
        if (!StringUtils.isEmpty(header)) {
            JSONObject json = JSON.parseObject(header);
            Set<String> key = json.keySet();
            for (String item : key) {
                map.put(item, json.getString(item));
            }
        }
        String param = notification.getParam();
        JSONObject json = new JSONObject();
        if (!StringUtils.isEmpty(param)) {
            json = JSONObject.parseObject(param);
        }
        json.put("qrcode", qrcode);
        logger.info("url: " + url + ", params: " + JSON.toJSONString(json));
        String r = HttpDeal.postJSONResponse(url, json, map);
        logger.info("response: " + r);
        result.setData(r);
        return result;
    }
}
