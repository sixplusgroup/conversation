package finley.gmair.dao.impl;

import finley.gmair.dao.BaseDao;
import finley.gmair.dao.TaskDao;
import finley.gmair.model.timing.Task;
import finley.gmair.util.IDGenerator;
import finley.gmair.util.ResponseCode;
import finley.gmair.util.ResultData;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public class TaskDaoImpl extends BaseDao implements TaskDao {
    @Override
    public ResultData insertTask(Task task) {
        ResultData result = new ResultData();
        task.setTaskId(IDGenerator.generate("GTI"));
        try {
            sqlSession.insert("gmair.timing.task.insert", task);
            result.setData(task);
        } catch (Exception e) {
            e.printStackTrace();
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription(e.getMessage());
        }
        return result;
    }

    @Override
    public ResultData queryTask(Map<String, Object> condition) {
        ResultData result = new ResultData();
        try {
            List<Task> list = sqlSession.selectList("gmair.timing.task.query", condition);
            if (list.isEmpty()) {
                result.setResponseCode(ResponseCode.RESPONSE_NULL);
            }
            result.setData(list);
        } catch (Exception e) {
            e.printStackTrace();
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription(e.getMessage());
        }
        return result;
    }

    @Override
    public ResultData updateTask(Task task) {
        ResultData result = new ResultData();
        try {
            sqlSession.update("gmair.timing.task.update", task);
            result.setData(task);
        } catch (Exception e) {
            e.printStackTrace();
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription(e.getMessage());
        }
        return result;
    }
}
