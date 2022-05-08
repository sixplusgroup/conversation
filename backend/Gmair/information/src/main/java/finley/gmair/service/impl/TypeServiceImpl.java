package finley.gmair.service.impl;

import finley.gmair.dao.knowledgebase.TypeMapper;
import finley.gmair.model.knowledgebase.Type;
import finley.gmair.service.TypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TypeServiceImpl implements TypeService {

    @Autowired
    TypeMapper typeMapper;

    @Override
    public List<Type> getAllTypes() {
        return typeMapper.getAllTypes();
    }
}
