package finley.gmair.service.impl;

import finley.gmair.dao.ComponentMeanDao;
import finley.gmair.model.dataAnalysis.ComponentMean;
import finley.gmair.service.ComponentMeanService;
import finley.gmair.util.ResultData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class ComponentMeanServiceImpl implements ComponentMeanService {

    @Autowired
    private ComponentMeanDao componentMeanDao;

    @Override
    public ResultData insertBatchDaily(List<ComponentMean> list){return componentMeanDao.insertBatch(list);}

    @Override
    public ResultData fetchDaily(Map<String,Object> condition){return componentMeanDao.query(condition);}
}
