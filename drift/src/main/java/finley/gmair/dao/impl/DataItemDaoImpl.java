package finley.gmair.dao.impl;

import com.alibaba.fastjson.JSONObject;
import finley.gmair.dao.BaseDao;
import finley.gmair.dao.DataItemDao;
import finley.gmair.model.drift.DataItem;
import finley.gmair.model.drift.DriftReport;
import finley.gmair.util.IDGenerator;
import finley.gmair.util.ResponseCode;
import finley.gmair.util.ResultData;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public class DataItemDaoImpl extends BaseDao implements DataItemDao {
    @Override
    public ResultData query(Map<String, Object> condition) {
        ResultData result = new ResultData();
        try {
            List<DriftReport> list = sqlSession.selectList("gmair.drift.dataitem.query", condition);
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
    public ResultData insert(DataItem dataItem) {
        ResultData result = new ResultData();
        dataItem.setItemId(IDGenerator.generate("DDI"));
        System.out.println(JSONObject.toJSONString(dataItem));
        try {
            sqlSession.insert("gmair.drift.dataitem.insert", dataItem);
            result.setData(dataItem);
        } catch (Exception e) {
            e.printStackTrace();
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription(e.getMessage());
        }
        return result;
    }
}
