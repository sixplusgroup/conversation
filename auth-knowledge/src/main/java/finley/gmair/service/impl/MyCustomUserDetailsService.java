package finley.gmair.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import finley.gmair.dao.PermissionMapper;
import finley.gmair.dao.UserMapper;
import finley.gmair.encode.Md5Encoder;
import finley.gmair.model.knowledgebaseAuth.KnowledgebasePermission;
import finley.gmair.model.knowledgebaseAuth.KnowledgebaseUser;
import org.springframework.security.core.userdetails.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class MyCustomUserDetailsService implements UserDetailsService {
    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    UserMapper userMapper;

    @Autowired
    PermissionMapper permissionMapper;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
////        List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
////        authorities.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
////        System.out.println(passwordEncoder.encode("goodGmair"));
////        UserDetails userDetails = new User("enduser",passwordEncoder.encode("password"),authorities);
////        return userDetails;

        List<SimpleGrantedAuthority> authorities = new LinkedList<>();
        Map<String, Object> condition = new HashMap<>();
        condition.put("name",username);
        List<KnowledgebaseUser> users = userMapper.query(condition);
        if (CollectionUtil.isEmpty(users)){
            throw new UsernameNotFoundException("no user");
        }
        KnowledgebaseUser user = users.get(0);
        List<Integer> PermissionIds = userMapper.getPermiisionId(user.getId());
        List<KnowledgebasePermission> permissions =  permissionMapper.queryByIds(PermissionIds);
        permissions.forEach(e->authorities.add(new SimpleGrantedAuthority(e.getAuthorize())));
        return new User(user.getName(),user.getPwd(), authorities);
    }
}