package finley.gmair.service.impl;

import finley.gmair.dao.AssignTypeInfoDao;
import finley.gmair.model.installation.AssignTypeInfo;
import finley.gmair.service.AssignTypeInfoService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author: Bright Chan
 * @date: 2021/3/13 15:50
 * @description: AssignTypeInfoServiceImpl
 */

@Service
public class AssignTypeInfoServiceImpl implements AssignTypeInfoService {

    @Resource
    private AssignTypeInfoDao assignTypeInfoDao;

    @Override
    public boolean create(AssignTypeInfo assignTypeInfo) {
        return assignTypeInfoDao.insert(assignTypeInfo);
    }

    @Override
    public boolean update(AssignTypeInfo assignTypeInfo) {
        return assignTypeInfoDao.update(assignTypeInfo);
    }

    @Override
    public boolean isValidType(String assignType) {
        return assignTypeInfoDao.isValidType(assignType);
    }

    @Override
    public List<AssignTypeInfo> queryAll() {
        return assignTypeInfoDao.queryAll();
    }

    @Override
    public AssignTypeInfo queryByAssignType(String assignType) {
        return assignTypeInfoDao.queryByAssignType(assignType);
    }
}
