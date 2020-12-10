package finley.gmair.dao;

import finley.gmair.model.mqttManagement.Model;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 机器型号的数据操作
 *
 * @author lycheeshell
 * @date 2020/12/9 00:26
 */
@Mapper
public interface ModelDao {

    /**
     * 新增机器型号
     *
     * @param model 机器型号信息
     * @return 新增条数
     */
    int insertModel(Model model);

    /**
     * 更新机器信息
     *
     * @param model 机器型号信息
     * @return  更新行数
     */
    int updateModel(Model model);

    /**
     * 根据型号id查询不包含其行为的型号信息
     *
     * @param modelId 型号id
     * @return 型号
     */
    Model queryOneWithoutAction(String modelId);

    /**
     * 查询不包含其行为的型号信息
     *
     * @param model 型号的查询条件
     * @return 型号列表
     */
    List<Model> queryModelsWithoutAction(Model model);

    /**
     * 新增型号与行为的对应关系
     *
     * @param modelId 型号id
     * @param actionId 行为id
     * @return 新增条数
     */
    int insertModelActionRelation(@Param("modelId") String modelId, @Param("actionId") String actionId);

    /**
     * 查询所有型号，包含其行为
     *
     * @return 型号列表
     */
    List<Model> queryModelsWithAction();

}
