package finley.gmair.service;

import finley.gmair.model.installation.AssignTypeInfo;

import java.util.List;

/**
 * @author: Bright Chan
 * @date: 2021/3/13 15:49
 * @description: AssignTypeInfoService
 */
public interface AssignTypeInfoService {

    boolean create(AssignTypeInfo assignTypeInfo);

    boolean update(AssignTypeInfo assignTypeInfo);

    boolean isValidType(String assignType);

    List<AssignTypeInfo> queryAll();

    AssignTypeInfo queryByAssignType(String assignType);
}
