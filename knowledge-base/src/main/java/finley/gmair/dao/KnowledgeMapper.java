package finley.gmair.dao;


import finley.gmair.model.knowledgebase.Knowledge;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface KnowledgeMapper {
    void insert(Knowledge knowledge);

    void delete(@Param("id") Integer id);

    //可能考虑全部用modify?
    void changeStatusTo2(@Param("id") Integer id);
    void changeStatusTo1(@Param("id") Integer id);
    void modify(Knowledge knowledge);

    Knowledge getById(@Param("id") Integer id);




}
