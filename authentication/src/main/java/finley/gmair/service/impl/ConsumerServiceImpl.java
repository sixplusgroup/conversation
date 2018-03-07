package finley.gmair.service.impl;

import finley.gmair.dao.ConsumerDao;
import finley.gmair.model.consumer.Consumer;
import finley.gmair.service.ConsumerService;
import finley.gmair.util.ResultData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ConsumerServiceImpl implements ConsumerService {
    @Autowired
    private ConsumerDao consumerDao;

    @Override
    public ResultData createConsumer(Consumer consumer) {
        ResultData result = new ResultData();

        return result;
    }
}
