package finley.gmair.netty;

import net.jodah.expiringmap.ExpirationPolicy;
import net.jodah.expiringmap.ExpiringMap;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.TimeUnit;


@Service
public class V1BoardRepository {
    private Map<String, String> board;

    public V1BoardRepository() {
        super();
        board = ExpiringMap.builder().expiration(5, TimeUnit.SECONDS).expirationPolicy(ExpirationPolicy.CREATED).build();
    }

    public V1BoardRepository push(String key, String value) {
        board.put(key, value);
        return this;
    }

    public String retrieve(String key) {
        return board.get(key);
    }
}
