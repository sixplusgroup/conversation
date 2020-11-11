package finley.gmair.dao.impl;

import finley.gmair.dao.BaseDao;
import finley.gmair.dao.MachineFilterInfoDao;
import finley.gmair.dto.MachineEfficientFilterInfo;
import finley.gmair.dto.MachinePrimaryFilterInfo;
import finley.gmair.dto.MachineTypeInfo;
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
    public List<MachineTypeInfo> fetchMachineTypeInfo() {
        try {
            return sqlSession.selectList("gmair.machine.machine_filter_info.query_machine_type");
        } catch (Exception e) {
            logger.error("fetchMachineTypeInfo failed: " + e.getMessage());
        }
        return new ArrayList<>();
    }
}
