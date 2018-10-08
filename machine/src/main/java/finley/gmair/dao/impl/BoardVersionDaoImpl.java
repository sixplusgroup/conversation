package finley.gmair.dao.impl;

import finley.gmair.dao.BaseDao;
import finley.gmair.dao.BoardVersionDao;
import finley.gmair.model.machine.BoardVersion;
import finley.gmair.util.ResponseCode;
import finley.gmair.util.ResultData;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Repository
public class BoardVersionDaoImpl extends BaseDao implements BoardVersionDao {

    @Override
    @Transactional
    public ResultData insertBoardVersion(BoardVersion version) {
        ResultData result = new ResultData();
        try {
            sqlSession.insert("gmair.machine.boardversion.insert", version);
            result.setData(version);
        } catch (Exception e) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription(e.getMessage());
            System.out.println(new StringBuffer(BoardVersionDaoImpl.class.getName()).append(" - error - ").append(e.getMessage()).toString());
        }
        return result;
    }

    @Override
    public ResultData queryBoardVersion(Map<String, Object> condition) {
        ResultData result = new ResultData();
        try {
            List<BoardVersion> list = sqlSession.selectList("gmair.machine.boardversion.query", condition);
            if (list.isEmpty()) {
                result.setResponseCode(ResponseCode.RESPONSE_NULL);
            }
            result.setData(list);
        } catch (Exception e) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription(e.getMessage());
            System.out.println(new StringBuffer(BoardVersionDaoImpl.class.getName()).append(" - error - ").append(e.getMessage()).toString());
        }
        return result;
    }

    @Override
    public ResultData delete(String machineId){
        ResultData result = new ResultData();
        try {
            sqlSession.delete("gmair.machine.boardversion.delete", machineId);
        } catch (Exception e) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription(e.getMessage());
        }
        return result;
    }
}
