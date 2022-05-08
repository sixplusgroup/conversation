package finley.gmair.dao.knowledgebase;


import finley.gmair.model.knowledgebase.Knowledge;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface KnowledgeMapper {
    void insert(Knowledge knowledge);

    void delete(@Param("id") Integer id);

    void changeStatus(@Param("status") Integer status, @Param("id")Integer id);
    void modify(Knowledge knowledge);

    Knowledge getById(@Param("id") Integer id);
    //List<Knowledge> getByType(@Param("knowledge_type") Integer knowledge_type);
    List<Knowledge> getByState(@Param("status") Integer status);
    @Select("select * from knowledge")
    List<Knowledge> getAll();
    List<Knowledge> getByIdList(@Param("list") List<Integer> list);
   // https://blog.csdn.net/u011781521/article/details/79669180
//https://stackoverflow.com/questions/37885076/lists-in-mybatis-in-clause

    void increaseViews(@Param("id") Integer id);

    List<Knowledge> search(@Param("key") String key);

    /**
     *@Description 选取id最大（最新的）keyword记录
     *@Author great fish
     *@Date  2022/4/25
     *
     * @return {@link null }
     */

    String getKeywords();
}
