package finley.gmair.service.impl;

import finley.gmair.model.auth.VerificationCode;
import finley.gmair.service.SerialService;
import finley.gmair.util.SerialUtil;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Map;
import java.util.TreeMap;

@Service
public class SerialServiceImpl implements SerialService {

    @Override
    @CachePut(value = "serials", key = "#phone", condition = "#phone != null")
    public VerificationCode generate(String phone) {
        return new VerificationCode(phone);
    }

    @Override
    @Cacheable(value = "serials", key = "#phone", unless = "#result != null")
    public VerificationCode fetch(String phone) {
        return null;
    }
}
