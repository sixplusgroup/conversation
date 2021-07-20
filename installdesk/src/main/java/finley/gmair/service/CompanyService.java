package finley.gmair.service;

import finley.gmair.model.installation.Company;
import finley.gmair.util.ResultData;

import java.util.Map;

public interface CompanyService {

    ResultData create(Company company);

    ResultData fetch(Map<String, Object> condition);
}
