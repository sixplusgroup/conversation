package finley.gmair.service;

import finley.gmair.model.knowledgebase.Type;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface TypeService {
    List<Type> getAllTypes();
}
