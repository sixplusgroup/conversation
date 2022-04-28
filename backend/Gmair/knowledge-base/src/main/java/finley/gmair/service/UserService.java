package finley.gmair.service;

import finley.gmair.model.knowledgebaseAuth.KnowledgebaseUser;
import org.springframework.stereotype.Service;

@Service
public interface UserService {
    void create(KnowledgebaseUser knowledgebaseUser);
}
