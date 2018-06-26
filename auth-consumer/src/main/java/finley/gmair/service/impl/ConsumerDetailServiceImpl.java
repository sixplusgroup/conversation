package finley.gmair.service.impl;

import finley.gmair.service.ConsumerService;
import finley.gmair.service.SerialService;
import finley.gmair.util.ResponseCode;
import finley.gmair.util.ResultData;
import finley.gmair.vo.consumer.ConsumerVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;


@Service
public class ConsumerDetailServiceImpl implements UserDetailsService{

    @Autowired
    ConsumerService consumerService;

    @Autowired
    SerialService serialService;

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        Map<String, Object> condition = new HashMap<>();
        Set<GrantedAuthority> grantedAuthorities = Set.of();
        condition.put("phone", s);
        ResultData response = consumerService.fetchConsumer(condition);
        if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
            ConsumerVo consumerVo = ((List<ConsumerVo>) response.getData()).get(0);
            return new User(consumerVo.getPhone(), serialService.fetch(s).getSerial(), grantedAuthorities);
        } else {
            condition.clear();
            condition.put("wechat", s);
            response = consumerService.fetchConsumer(condition);
            if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
                ConsumerVo consumerVo = ((List<ConsumerVo>) response.getData()).get(0);
                return new User(consumerVo.getPhone(), "", grantedAuthorities);
            } else {
                throw new UsernameNotFoundException("user not exists");
            }
        }
    }
}
