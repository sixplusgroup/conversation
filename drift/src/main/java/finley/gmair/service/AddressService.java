package finley.gmair.service;

import finley.gmair.model.drift.DriftAddress;
import finley.gmair.util.ResultData;

import java.util.Map;

public interface AddressService {
    ResultData createAddress(DriftAddress address);

    ResultData fetchAddress(Map<String, Object> condition);

    ResultData updateAddress(DriftAddress address);

    ResultData blockAddress(String addressId);

}
