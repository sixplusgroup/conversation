package finley.gmair.dao.impl;

import finley.gmair.dao.BaseDao;
import finley.gmair.dao.PicDao;
import finley.gmair.model.installation.Member;
import finley.gmair.model.installation.Pic;
import finley.gmair.util.IDGenerator;
import finley.gmair.util.ResponseCode;
import finley.gmair.util.ResultData;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


@Repository
public class PicDaoImpl extends BaseDao implements PicDao {
    @Override
    public ResultData insertPic(Pic pic) {

        ResultData result = new ResultData();
        pic.setPicId(IDGenerator.generate("IPC"));
        try {
            sqlSession.insert("gmair.installation.pic.insert", pic);
            result.setData(pic);
        } catch (Exception e) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription(e.getMessage());
        }
        return result;
    }

    @Override
    public ResultData queryPic(Map<String, Object> condition) {
        ResultData result = new ResultData();
        List<Member> list = new ArrayList<>();
        try {
            list = sqlSession.selectList("gmair.installation.pic.query", condition);
            result.setData(list);

        } catch (Exception e) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription(e.getMessage());
        }

        if (result.getResponseCode() != ResponseCode.RESPONSE_ERROR) {
            if (list.isEmpty() == true) {
                result.setResponseCode(ResponseCode.RESPONSE_NULL);
                result.setDescription("No pic found");
            } else {
                result.setResponseCode(ResponseCode.RESPONSE_OK);
                result.setDescription("Success to found pic");
            }
        }
        return result;
    }

    @Override
    public ResultData deletePic(Map<String, Object> condition) {
        ResultData result = new ResultData();
        try {
            sqlSession.delete("gmair.installation.pic.delete", condition);
            result.setResponseCode(ResponseCode.RESPONSE_OK);
        } catch (Exception e) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription(e.getMessage());
        }
        return result;
    }

    @Override
    public ResultData updatePic(Pic pic) {
        ResultData result = new ResultData();
        try {
            sqlSession.update("gmair.installation.pic.update", pic);
            result.setResponseCode(ResponseCode.RESPONSE_OK);
        } catch (Exception e){
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription(e.getMessage());
        }
        return result;
    }

}
