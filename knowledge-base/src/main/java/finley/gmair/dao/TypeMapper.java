package finley.gmair.dao;

import finley.gmair.model.knowledgebase.Knowledge;
import finley.gmair.model.knowledgebase.Type;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface TypeMapper {
    @Select("select * from knowledge_type")
    List<Type> getAllTypes();
}
