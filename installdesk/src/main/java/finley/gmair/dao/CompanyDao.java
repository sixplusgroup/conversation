package finley.gmair.dao;

import finley.gmair.model.installation.Company;
import finley.gmair.util.ResultData;

import java.util.Map;

public interface CompanyDao {

    ResultData query(Map<String, Object> condition);

    ResultData insert(Company company);
}
