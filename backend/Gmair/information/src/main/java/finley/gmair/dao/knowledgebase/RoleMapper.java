package finley.gmair.dao.knowledgebase;


import finley.gmair.model.knowledgebaseAuth.KnowledgebaseRole;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface RoleMapper {
    List<KnowledgebaseRole> query(Map<String, Object> condition);
}
