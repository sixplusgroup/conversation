package finley.gmair.service.impl;

import finley.gmair.dao.ModelDao;
import finley.gmair.model.mqttManagement.Model;
import finley.gmair.service.ModelService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author lycheeshell
 * @date 2020/12/9 01:28
 */
@Service
public class ModelServiceImpl implements ModelService {

    @Resource
    private ModelDao modelDao;

    /**
     * 查询所有型号，包含其行为
     *
     * @return 型号列表
     */
    @Override
    public List<Model> queryModelsWithAction() {
        return modelDao.queryModelsWithAction();
    }

}
