package finley.gmair.dao.impl;

import finley.gmair.dao.AssignTypeInfoDao;
import finley.gmair.dao.BaseDao;
import finley.gmair.model.installation.AssignTypeInfo;
import finley.gmair.util.IDGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

/**
 * @author: Bright Chan
 * @date: 2021/3/13 15:20
 * @description: AssignTypeInfoDaoImpl
 */

@Repository
public class AssignTypeInfoDaoImpl extends BaseDao implements AssignTypeInfoDao {

    private Logger logger = LoggerFactory.getLogger(AssignTypeInfoDaoImpl.class);

    @Override
    public boolean insert(AssignTypeInfo assignTypeInfo) {
        assignTypeInfo.setAssignTypeInfoId(IDGenerator.generate("IATI"));
        int res = 0;
        try {
            res = sqlSession.insert("gmair.install.assign_type_info.insert", assignTypeInfo);
        } catch (Exception e) {
            logger.error("assign type insert failed: " + e.getMessage());
        }
        return res > 0;
    }

    @Override
    public boolean update(AssignTypeInfo assignTypeInfo) {
        int res = 0;
        try {
            res = sqlSession.update("gmair.install.assign_type_info.update", assignTypeInfo);
        } catch (Exception e) {
            logger.error("assign type update failed: " + e.getMessage());
        }
        return res > 0;
    }

    @Override
    public boolean isValidType(String assignType) {
        int res = 0;
        try {
            res = sqlSession.selectOne("gmair.install.assign_type_info.query_size_by_assign_type",
                                                  assignType);
        } catch (Exception e) {
            logger.error("query assign type is valid or not failed: " + e.getMessage());
        }
        return res > 0;
    }

    @Override
    public List<AssignTypeInfo> queryAll() {
        List<AssignTypeInfo> res = new ArrayList<>();
        try {
            res = sqlSession.selectList("gmair.install.assign_type_info.query_all");
        } catch (Exception e) {
            logger.error("query all assign type failed: " + e.getMessage());
        }
        return res;
    }

    @Override
    public AssignTypeInfo queryByAssignType(String assignType) {
        AssignTypeInfo res = new AssignTypeInfo();
        try {
            res = sqlSession.selectOne("gmair.install.assign_type_info.query_by_assign_type",
                                                  assignType);
        } catch (Exception e) {
            logger.error("query by assign type failed: " + e.getMessage());
        }
        return res;
    }
}
