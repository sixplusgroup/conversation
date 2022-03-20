package finley.gmair.dao;


import finley.gmair.model.knowledgebase.Knowledge;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface KnowledgeMapper {
    int insert(Knowledge knowledge);

    int delete(@Param("id") Integer id);
}
