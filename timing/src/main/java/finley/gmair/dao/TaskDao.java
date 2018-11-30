package finley.gmair.dao;

import finley.gmair.model.timing.Task;
import finley.gmair.util.ResultData;

import java.util.Map;

public interface TaskDao {
    ResultData insertTask(Task task);

    ResultData queryTask(Map<String, Object> condition);

    ResultData updateTask(Task task);
}
