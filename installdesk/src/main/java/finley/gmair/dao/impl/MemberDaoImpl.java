package finley.gmair.dao.impl;

import com.alibaba.fastjson.JSONObject;
import finley.gmair.dao.BaseDao;
import finley.gmair.dao.MemberDao;
import finley.gmair.model.installation.Member;
import finley.gmair.util.IDGenerator;
import finley.gmair.util.ResponseCode;
import finley.gmair.util.ResultData;
import org.apache.ibatis.session.RowBounds;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * @ClassName: MemberDaoImpl
 * @Description: TODO
 * @Author fan
 * @Date 2019/4/1 6:05 PM
 */
@Repository
public class MemberDaoImpl extends BaseDao implements MemberDao {
    private Logger logger = LoggerFactory.getLogger(MemberDaoImpl.class);

    @Override
    public ResultData insert(Member member) {
        ResultData result = new ResultData();
        member.setMemberId(IDGenerator.generate("ISM"));
        try {
            sqlSession.insert("gmair.install.member.insert", member);
            result.setData(member);
        } catch (Exception e) {
            logger.error(e.getMessage());
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription(e.getMessage());
        }
        return result;
    }

    @Override
    public ResultData query(Map<String, Object> condition) {
        ResultData result = new ResultData();
        try {
            List list = sqlSession.selectList("gmair.install.member.query", condition);
            if (list.isEmpty()) {
                result.setResponseCode(ResponseCode.RESPONSE_NULL);
            }
            result.setData(list);
        } catch (Exception e) {
            logger.error(e.getMessage());
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription(e.getMessage());
        }
        return result;
    }

    @Override
    public ResultData query(Map<String, Object> condition, int start, int length) {
        ResultData result = new ResultData();
        JSONObject data = new JSONObject();
        try {
            List list = sqlSession.selectList("gmair.install.member.query", condition);
            data.put("size", list.size());
            list = sqlSession.selectList("gmair.install.member.query", condition, new RowBounds(start, length));
            data.put("data", list);
        } catch (Exception e) {
            logger.error(e.getMessage());
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription(e.getMessage());
        }
        return result;
    }

    @Override
    public ResultData update(Map<String, Object> condition) {
        ResultData result = new ResultData();
        try {
            sqlSession.update("gmair.install.member.update", condition);
        } catch (Exception e) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription(e.getMessage());
        }
        return result;
    }

    @Override
    public ResultData block(String memberId) {
        ResultData result = new ResultData();
        try {
            sqlSession.update("gmair.install.member.block", memberId);
        } catch (Exception e) {
            logger.error("[Error] " + e.getMessage());
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription(e.getMessage());
        }
        return result;
    }

    @Override
    public ResultData remove(String memberId) {
        ResultData result = new ResultData();
        try {
            sqlSession.delete("gmair.install.member.remove", memberId);
        } catch (Exception e) {
            logger.error("[Error] " + e.getMessage());
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription(e.getMessage());
        }
        return result;
    }
}
