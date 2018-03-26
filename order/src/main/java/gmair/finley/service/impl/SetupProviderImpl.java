package gmair.finley.service.impl;

import finley.gmair.model.order.SetupProvider;
import finley.gmair.util.ResponseCode;
import finley.gmair.util.ResultData;
import gmair.finley.dao.SetupProviderDao;
import gmair.finley.service.SetupProviderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;


@Service
public class SetupProviderImpl implements SetupProviderService{

    @Autowired
    private SetupProviderDao setupProviderDao;

    @Override
    public ResultData fetchMissionChannel(Map<String, Object> condition) {
        return setupProviderDao.query(condition);
    }

    @Override
    public ResultData create(SetupProvider provider) {
        return setupProviderDao.insert(provider);
    }

    @Override
    public ResultData modifyMissionChannel(SetupProvider provider) {
        ResultData result = new ResultData();
        ResultData response = setupProviderDao.update(provider);
        result.setResponseCode(response.getResponseCode());
        if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
            result.setData(response.getData());
        } else {
            result.setDescription(response.getDescription());
        }
        return result;
    }

    @Override
    public ResultData deleteMissionChannel(String channelId) {
        ResultData result = setupProviderDao.delete(channelId);
        return result;
    }
}
