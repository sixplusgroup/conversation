package finley.gmair.service;

import finley.gmair.po.Knowledge;
import org.springframework.stereotype.Service;

@Service
public interface KnowledgeService {
    void insert(Knowledge knowledge);
}
