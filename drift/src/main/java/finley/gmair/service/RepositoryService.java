package finley.gmair.service;

import finley.gmair.model.drift.DriftRepository;
import finley.gmair.util.ResultData;

import java.util.Map;

public interface RepositoryService {
    ResultData fetchRepository(Map<String, Object> condition);

    ResultData createRepository(DriftRepository repository);

    ResultData modifyRepository(Map<String, Object> condition);
}
