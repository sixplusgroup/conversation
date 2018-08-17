package finley.gmair.dao.impl;

import finley.gmair.dao.MerchantProfileDao;
import finley.gmair.model.enterpriseselling.MerchantProfile;
import finley.gmair.util.IDGenerator;
import finley.gmair.util.ResponseCode;
import finley.gmair.util.ResultData;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public class MerchantProfileDaoImpl extends BaseDao implements MerchantProfileDao {

    @Override
    public ResultData insert(MerchantProfile merchantProfile){
        ResultData result = new ResultData();
        merchantProfile.setMerchantId(IDGenerator.generate("MRT"));
        try{
            sqlSession.insert("gmair.enterprise_selling.merchant_profile.insert",merchantProfile);
            result.setData(merchantProfile);
        }catch (Exception e){
            e.printStackTrace();
            result.setDescription(e.getMessage());
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
        }
        return result;
    }

    @Override
    public ResultData query(Map<String, Object> condition){
        ResultData result = new ResultData();
        try{
            List<MerchantProfile> list = sqlSession.selectList("gmair.enterprise_selling.merchant_profile.query",condition);
            if (list.isEmpty()) {
                result.setResponseCode(ResponseCode.RESPONSE_NULL);
            }
            result.setData(list);
        }catch (Exception e){
            e.printStackTrace();
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription(e.getMessage());
        }
        return result;
    }

    @Override
    public ResultData update(Map<String, Object> condition){
        ResultData result = new ResultData();
        try{
            sqlSession.update("gmair.enterprise_selling.merchant_profile.update",condition);
        }catch (Exception e){
            e.printStackTrace();
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription(e.getMessage());
        }
        return result;
    }
}
