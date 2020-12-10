package finley.gmair.service.impl;

import finley.gmair.dao.ModelDao;
import finley.gmair.model.mqttManagement.Model;
import finley.gmair.service.ModelService;
import finley.gmair.util.IDGenerator;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * 机器型号服务
 *
 * @author lycheeshell
 * @date 2020/12/10 19:02
 */
@Service
public class ModelServiceImpl implements ModelService {

    @Resource
    private ModelDao modelDao;

    /**
     * 保存机器型号
     *
     * @param name        型号标识名称的英文字符串
     * @param description 机器型号的描述说明
     * @return 影响条数
     */
    @Override
    public int saveModel(String name, String description) throws Exception {
        //检查该机器型号是否已经存在
        Model queryModel = new Model();
        queryModel.setName(name);
        List<Model> existModels = modelDao.queryModelsWithoutAction(queryModel);
        if (existModels != null && existModels.size() > 1) {
            throw new Exception("该名称的机器型号数量超过一个");
        }

        int affectedLines;

        Model model = new Model();
        if (null == existModels || existModels.size() == 0) {
            //不存在则新增机器
            model.setModelId(IDGenerator.generate("MODL"));
            model.setName(name);
            model.setDescription(description);
            affectedLines = modelDao.insertModel(model);
        } else {
            //存在则更新机器描述
            model.setModelId(existModels.get(0).getModelId());
            model.setDescription(description);
            affectedLines = modelDao.updateModel(model);
        }

        return affectedLines;
    }

    /**
     * 根据型号id查询不包含其行为的型号信息
     *
     * @param modelId 型号id
     * @return 型号
     */
    @Override
    public Model queryOneWithoutAction(String modelId) {
        return modelDao.queryOneWithoutAction(modelId);
    }

    /**
     * 查询不包含其行为的型号信息
     *
     * @param name 型号标识名称的英文字符串
     * @return 型号列表
     */
    @Override
    public List<Model> queryModelsWithoutAction(String name) {
        Model queryModel = new Model();
        queryModel.setName(name);
        return modelDao.queryModelsWithoutAction(queryModel);
    }

    /**
     * 新增型号与行为的对应关系
     *
     * @param modelId  型号id
     * @param actionId 行为id
     * @return 新增条数
     */
    @Override
    public int insertModelActionRelation(String modelId, String actionId) {
        return modelDao.insertModelActionRelation(modelId, actionId);
    }

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
