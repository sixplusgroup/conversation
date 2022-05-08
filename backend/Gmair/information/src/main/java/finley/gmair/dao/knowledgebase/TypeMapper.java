package finley.gmair.dao.knowledgebase;

import finley.gmair.model.knowledgebase.Knowledge;
import finley.gmair.model.knowledgebase.Type;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TypeMapper {
    @Select("select * from knowledge_type")
    List<Type> getAllTypes();
}
