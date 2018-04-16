package finley.gmair.service;

import java.util.Map;

public interface SerialService {
    Map<String, String> generate(String phone);

    Map<String, String> fetch(String phone);
}
