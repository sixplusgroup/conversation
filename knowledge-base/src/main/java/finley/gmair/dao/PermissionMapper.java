package finley.gmair.dao;

import finley.gmair.model.knowledgebaseAuth.KnowledgebasePermission;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Mapper
@Repository
public interface PermissionMapper {
    List<KnowledgebasePermission> query(Map<String, Object> condition);

    List<KnowledgebasePermission> queryByIds(List<Integer> ids);
}
