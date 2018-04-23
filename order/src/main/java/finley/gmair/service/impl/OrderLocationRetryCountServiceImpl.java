package finley.gmair.service.impl;

import finley.gmair.dao.OrderLocationRetryCountDao;
import finley.gmair.model.location.OrderLocationRetryCount;
import finley.gmair.service.OrderLocationRetryCountService;
import finley.gmair.util.ResultData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class OrderLocationRetryCountServiceImpl implements OrderLocationRetryCountService{

    @Autowired
    OrderLocationRetryCountDao orderLocationRetryCountDao;

    @Override
    public ResultData insert(OrderLocationRetryCount orderLocationRetryCount) {
        return orderLocationRetryCountDao.insert(orderLocationRetryCount);
    }

    @Override
    public ResultData insertBatch(List<OrderLocationRetryCount> list) {
        return orderLocationRetryCountDao.insertBatch(list);
    }

    @Override
    public ResultData fetch(Map<String, Object> condition) {
        return orderLocationRetryCountDao.query(condition);
    }

    @Override
    public ResultData update(OrderLocationRetryCount orderLocationRetryCount) {
        return orderLocationRetryCountDao.update(orderLocationRetryCount);
    }

    @Override
    public ResultData delete(Map<String, Object> condition) {
        return orderLocationRetryCountDao.delete(condition);
    }
}
