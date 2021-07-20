package finley.gmair.service.impl;

import finley.gmair.dao.UserassignDao;
import finley.gmair.dao.impl.UserassignDaoImpl;
import finley.gmair.model.installation.Userassign;
import finley.gmair.service.UserassignService;
import finley.gmair.util.ResultData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class UserassignServiceImpl implements UserassignService {
    @Autowired
    private UserassignDao userassignDao;

    @Override
    public ResultData insert(Userassign userassign) {
        userassign.setUserassignStatus(0);
        ResultData response = userassignDao.insert(userassign);
        return response;
    }

    @Override
    public ResultData fetch(Map<String, Object> condition) {
        ResultData response = userassignDao.query(condition);
        return response;
    }

    @Override
    public ResultData fetch(Map<String, Object> condition, int start, int length) {
        ResultData response = userassignDao.query(condition, start, length);
        return response;
    }

    @Override
    public ResultData principal(Map<String, Object> condition) {
        ResultData response = userassignDao.principal(condition);
        return response;
    }

    @Override
    public ResultData principal(Map<String, Object> condition, int start, int length) {
        ResultData response = userassignDao.principal(condition, start, length);
        return response;
    }

    @Override
    public ResultData confirmReservation(String userassignId) {
        ResultData response = userassignDao.updateUserassignStatus(userassignId);
        return response;
    }

    @Override
    public ResultData adjust(Userassign userassign) {
        ResultData response = userassignDao.update(userassign);
        return response;
    }
}
