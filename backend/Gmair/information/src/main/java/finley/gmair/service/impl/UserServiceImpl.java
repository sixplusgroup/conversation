package finley.gmair.service.impl;

import finley.gmair.dao.knowledgebase.UserAssignmentMapper;
import finley.gmair.dao.knowledgebase.UserMapper;
import finley.gmair.model.knowledgebaseAuth.KnowledgebaseUser;
import finley.gmair.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    UserMapper userMapper;

    @Autowired
    UserAssignmentMapper userAssignmentMapper;

    @Override
    public void create(KnowledgebaseUser knowledgebaseUser){
        userMapper.create(knowledgebaseUser);
        userAssignmentMapper.insert(knowledgebaseUser.getId(),3); //插入用户
    }
}
