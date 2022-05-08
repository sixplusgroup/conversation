package finley.gmair.dao.knowledgebase;

import finley.gmair.model.knowledgebaseAuth.KnowledgebaseUser;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface UserMapper {
    List<KnowledgebaseUser> query(Map<String, Object> condition);

    List<Integer> getPermiisionId(int userId);

    int create(KnowledgebaseUser knowledgebaseUser);
}
