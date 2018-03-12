package finley.gmair.service.impl;

import finley.gmair.service.SerialService;
import finley.gmair.util.SerialUtil;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Map;
import java.util.TreeMap;

@Service
public class SerialServiceImpl implements SerialService {
    private Map<String, String> serials = new TreeMap<>();

    @Override
    @Cacheable("serials")
    public Map<String, String> generate(String phone) {
        Map<String, String> result = new TreeMap<>();
        String serial = SerialUtil.serial();
        result.put(phone, serial);
        serials.put(phone, serial);
        return result;
    }

    @Override
    @Cacheable("serials")
    public Map<String, String> fetch(String phone) {
        Map<String, String> result = new TreeMap<>();
        String serial = serials.get(phone);
        if (StringUtils.isEmpty(serial)) {
            return null;
        }
        result.put(phone, serial);
        return result;
    }
}
