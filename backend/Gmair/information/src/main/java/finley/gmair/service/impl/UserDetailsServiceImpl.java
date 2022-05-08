package finley.gmair.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import finley.gmair.dao.knowledgebase.PermissionMapper;
import finley.gmair.dao.knowledgebase.UserMapper;
import finley.gmair.enums.knowledgeBase.KnowledgebaseUserType;
import finley.gmair.model.knowledgebaseAuth.KnowledgebasePermission;
import finley.gmair.model.knowledgebaseAuth.KnowledgebaseUser;
import finley.gmair.oauth2.CustomUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    UserMapper userMapper;

    @Autowired
    PermissionMapper permissionMapper;
    @Override
    public UserDetails loadUserByUsername(String name) throws UsernameNotFoundException{
        List<SimpleGrantedAuthority> authorities = new LinkedList<>();
        Map<String, Object> condition = new HashMap<>();
        condition.put("name",name);
        List<KnowledgebaseUser> users = userMapper.query(condition);
        if (CollectionUtil.isEmpty(users)){
            throw new UsernameNotFoundException("no user");
        }
        KnowledgebaseUser user = users.get(0);
        List<Integer> PermissionIds = userMapper.getPermiisionId(user.getId());
        List<KnowledgebasePermission> permissions =  permissionMapper.queryByIds(PermissionIds);
        permissions.forEach(e->authorities.add(new SimpleGrantedAuthority(e.getAuthorize())));
        return new CustomUser(user.getName(),user.getPwd(),KnowledgebaseUserType.getValueByCode(user.getType()),user.getId(),authorities);
    }
}
