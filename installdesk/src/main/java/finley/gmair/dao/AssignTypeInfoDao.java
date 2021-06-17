package finley.gmair.dao;

import finley.gmair.model.installation.AssignTypeInfo;

import java.util.List;

/**
 * @author: Bright Chan
 * @date: 2021/3/13 15:11
 * @description: AssignTypeInfoDao
 */
public interface AssignTypeInfoDao {

    boolean insert(AssignTypeInfo assignTypeInfo);

    boolean update(AssignTypeInfo assignTypeInfo);

    boolean isValidType(String assignType);

    List<AssignTypeInfo> queryAll();

    AssignTypeInfo queryByAssignType(String assignType);
}
