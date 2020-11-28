package finley.gmair.dao.impl;

import finley.gmair.dao.BaseDao;
import finley.gmair.dao.MachineEfficientFilterConfigDao;
import finley.gmair.model.machine.MachineEfficientFilterConfig;
import finley.gmair.util.IDGenerator;
import finley.gmair.vo.machine.FilterUpdByFormulaConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author: Bright Chan
 * @date: 2020/11/28 11:23
 * @description: MachineEfficientFilterConfigDaoImpl
 */

@Repository
public class MachineEfficientFilterConfigDaoImpl extends BaseDao
                                                    implements MachineEfficientFilterConfigDao {

    private Logger logger = LoggerFactory.getLogger(MachineEfficientFilterConfigDaoImpl.class);

    @Override
    public void insert(MachineEfficientFilterConfig config) {
        config.setConfigId(IDGenerator.generate("MEF"));
        try {
            sqlSession.insert("gmair.machine.machine_efficient_filter_config.insert", config);
        }catch (Exception e) {
            logger.error("insert MachineEfficientFilterConfig failed: " + e.getMessage());
        }
    }

    @Override
    public List<MachineEfficientFilterConfig> queryByModelId(String modelId) {
        Map<String, Object> condition = new HashMap<>();
        condition.put("blockFlag", false);
        condition.put("modelId", modelId);
        try {
            return sqlSession.selectList
                    ("gmair.machine.machine_efficient_filter_config.queryByModelId", condition);
        } catch (Exception e) {
            logger.error("queryByModelId failed: " + e.getMessage());
        }
        return new ArrayList<>();
    }

    @Override
    public List<FilterUpdByFormulaConfig> queryConfigList() {
        try {
            return sqlSession.selectList
                    ("gmair.machine.machine_efficient_filter_config.queryConfigList");
        } catch (Exception e) {
            logger.error("queryConfigList failed: " + e.getMessage());
        }
        return new ArrayList<>();
    }

    @Override
    public void update(Map<String, Object> condition) {
        try{
            sqlSession.update("gmair.machine.machine_efficient_filter_config.update",
                                        condition);
        }catch (Exception e){
            logger.error("update failed: " + e.getMessage());
        }
    }
}
