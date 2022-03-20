package finley.gmair.service;

import finley.gmair.model.knowledgebase.Knowledge;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Service;

@Service
public interface KnowledgeService {
    void insert(Knowledge knowledge);

    void delete(Integer id);
}
