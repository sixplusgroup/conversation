package finley.gmair.dao.impl;

import finley.gmair.dao.BaseDao;
import finley.gmair.dao.MachineFilterInfoDao;
import finley.gmair.dto.MachineEfficientFilterInfo;
import finley.gmair.dto.MachinePrimaryFilterInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author: Bright Chan
 * @date: 2020/11/9 13:45
 * @description: MachineFilterInfoDaoImpl
 */

@Repository
public class MachineFilterInfoDaoImpl extends BaseDao implements MachineFilterInfoDao {

    private Logger logger = LoggerFactory.getLogger(MachineFilterInfoDaoImpl.class);

    @Override
    public List<MachinePrimaryFilterInfo> fetchMachinePrimaryFilterInfo(Map<String, Object> condition) {
        try {
            return sqlSession.selectList("gmair.machine.machine_filter_info.query_primary", condition);
        } catch (Exception e) {
            logger.error("fetchMachinePrimaryFilterInfo failed: " + e.getMessage());
        }
        return new ArrayList<>();
    }

    @Override
    public List<MachineEfficientFilterInfo> fetchMachineEfficientFilterInfo(Map<String, Object> condition) {
        try {
            return sqlSession.selectList("gmair.machine.machine_filter_info.query_efficient", condition);
        } catch (Exception e) {
            logger.error("fetchMachineEfficientFilterInfo failed: " + e.getMessage());
        }
        return new ArrayList<>();
    }

    @Override
    public List<String> fetchMachineModelName() {
        try {
            return sqlSession.selectList("gmair.machine.machine_filter_info.query_model_name");
        } catch (Exception e) {
            logger.error("fetchMachineModelName failed: " + e.getMessage());
        }
        return new ArrayList<>();
    }

    @Override
    public List<String> fetchMachineModelCode(String modelName) {
        try {
            return sqlSession.selectList("gmair.machine.machine_filter_info.query_model_code", modelName);
        } catch (Exception e) {
            logger.error("fetchMachineModelCode failed: " + e.getMessage());
        }
        return new ArrayList<>();
    }
}
