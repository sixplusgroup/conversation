package finley.gmair.service.impl;

import finley.gmair.model.admin.Admin;
import finley.gmair.service.ConsumerService;
import finley.gmair.util.ResponseCode;
import finley.gmair.util.ResultData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ConsumerDetailServiceImpl implements UserDetailsService {

    @Autowired
    private ConsumerService consumerService;

    @Override
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
        List<SimpleGrantedAuthority> authorities = List.of();
        Map<String, Object> condition = new HashMap<>();
        condition.put("username", userName);
        ResultData resultData = consumerService.fetchConsumer(condition);
        if (resultData.getResponseCode() != ResponseCode.RESPONSE_OK) {
            //use email login
            condition.clear();
            condition.put("email", userName);
            resultData = consumerService.fetchConsumer(condition);
            if (resultData.getResponseCode() != ResponseCode.RESPONSE_OK) {
                throw new UsernameNotFoundException("no user");
            }
            Admin admin = ((List<Admin>) resultData.getData()).get(0);
            return new User(admin.getEmail(), admin.getPassword(), authorities);
        }
        //user name login
        Admin admin = ((List<Admin>) resultData.getData()).get(0);
        return new User(admin.getUsername(), admin.getPassword(), authorities);
    }
}
