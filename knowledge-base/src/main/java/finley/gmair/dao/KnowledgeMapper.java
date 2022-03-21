package finley.gmair.dao;


import finley.gmair.model.knowledgebase.Knowledge;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface KnowledgeMapper {
    void insert(Knowledge knowledge);

    void delete(@Param("id") Integer id);

    //
    void changeStatusTo2(@Param("id") Integer id);
    void changeStatusTo1(@Param("id") Integer id);
    void modify(Knowledge knowledge);

    Knowledge getById(@Param("id") Integer id);
    List<Knowledge> getByType(@Param("knowledge_type") Integer knowledge_type);
    List<Knowledge> getByState(@Param("state") Integer state);
    @Select("select * from document")
    List<Knowledge> getAll();

    void increaseViews(@Param("id") Integer id);
}
