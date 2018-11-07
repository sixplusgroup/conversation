package finley.gmair.dao.impl;

import finley.gmair.dao.BaseDao;
import finley.gmair.dao.MachinePicDao;
import finley.gmair.dao.PicDao;
import finley.gmair.model.installation.MachinePic;
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
public class MachinePicDaoImpl extends BaseDao implements MachinePicDao {
    @Override
    public ResultData insertMachinePic(MachinePic machinePic) {

        ResultData result = new ResultData();
        machinePic.setRecordId(IDGenerator.generate("RCD"));
        try {
            sqlSession.insert("gmair.installation.install_machine_pic.insert", machinePic);
            result.setData(machinePic);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription(e.getMessage());
        }
        return result;
    }

    @Override
    public ResultData queryMachinePic(Map<String, Object> condition) {
        ResultData result = new ResultData();
        List<MachinePic> list = new ArrayList<>();
        try {
            list = sqlSession.selectList("gmair.installation.install_machine_pic.query", condition);
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
    public ResultData deleteMachinePic(Map<String, Object> condition) {
        ResultData result = new ResultData();
        try {
            sqlSession.delete("gmair.installation.install_machine_pic.delete", condition);
            result.setResponseCode(ResponseCode.RESPONSE_OK);
        } catch (Exception e) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription(e.getMessage());
        }
        return result;
    }
}
