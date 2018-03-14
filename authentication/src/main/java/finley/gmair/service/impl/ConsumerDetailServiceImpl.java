package finley.gmair.service.impl;

import finley.gmair.service.ConsumerService;
import finley.gmair.util.ResponseCode;
import finley.gmair.util.ResultData;
import finley.gmair.vo.consumer.ConsumerVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
public class ConsumerDetailServiceImpl implements UserDetailsService{

    private static final Logger logger = LoggerFactory.getLogger(ConsumerDetailServiceImpl.class);

    @Autowired
    ConsumerService consumerService;

    @Override
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
        logger.debug("Load user by username: " + userName);
        List<SimpleGrantedAuthority> authorities = List.of();
        Map<String, Object> condition = new HashMap<>();
        condition.put("phone", userName);
        ResultData resultData = consumerService.queryConsumer(condition);
        if (resultData.getResponseCode() != ResponseCode.RESPONSE_OK) {
            logger.error("User not found");
            throw new UsernameNotFoundException("User not found");
        }
        ConsumerVo consumer = ((List<ConsumerVo>) resultData.getData()).get(0);

        return new User(consumer.getUsername(), "123456", authorities);
    }
}
