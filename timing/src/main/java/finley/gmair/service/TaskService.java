package finley.gmair.service;

import finley.gmair.model.timing.Task;
import finley.gmair.util.ResultData;

import java.util.Map;

public interface TaskService {
    ResultData fetch(Map<String, Object> condition);

    ResultData create(Task task);

    ResultData modify(Task task);

    boolean probeTaskStatus(Map<String, Object> condition);
}
