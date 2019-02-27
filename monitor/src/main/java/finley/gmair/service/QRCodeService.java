package finley.gmair.service;

import finley.gmair.util.ResultData;

import java.util.Map;

public interface QRCodeService {
    ResultData fetchProfile(Map<String, Object> condition);

    ResultData fetchAds(Map<String, Object> condition);
}
