package finley.gmair.dao;


import finley.gmair.po.Knowledge;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface KnowledgeMapper {
    int insert(Knowledge knowledge);
}
