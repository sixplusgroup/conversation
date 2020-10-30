package finley.gmair.service.impl;

import finley.gmair.model.auth.VerificationCode;
import finley.gmair.service.SerialService;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
public class SerialServiceImpl implements SerialService {

    @Override
    @Cacheable(value = "serials", key = "#phone", condition = "#phone != null")
    public VerificationCode generate(String phone) {
        return new VerificationCode(phone);
    }

    @Override
    @Cacheable(value = "serials", key = "#phone", unless = "#result != null")
    public VerificationCode fetch(String phone) {
        return null;
    }
}
