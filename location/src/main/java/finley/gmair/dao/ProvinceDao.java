package finley.gmair.dao;

import finley.gmair.model.district.Province;
import finley.gmair.util.ResultData;

import java.util.Map;

public interface ProvinceDao {
    ResultData insertProvince(Province province);

    ResultData queryProvince(Map<String, Object> condition);

    ResultData updateProvince(Province province);
}
