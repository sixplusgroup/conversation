package finley.gmair.dao.impl;

import finley.gmair.dao.MerchantContactDao;
import finley.gmair.model.enterpriseselling.MerchantContact;
import finley.gmair.util.IDGenerator;
import finley.gmair.util.ResponseCode;
import finley.gmair.util.ResultData;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public class MerchantContactDaoImpl extends BaseDao implements MerchantContactDao {

    @Override
    public ResultData insert(MerchantContact merchantContact){
        ResultData result = new ResultData();
        merchantContact.setContactId(IDGenerator.generate("CTD"));
        try{
            sqlSession.insert("gmair.enterprise_selling.merchant_contact.insert",merchantContact);
            result.setData(merchantContact);
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
            List<MerchantContact> list = sqlSession.selectList("gmair.enterprise_selling.merchant_contact.query",condition);
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
            sqlSession.update("gmair.enterprise_selling.merchant_contact.update",condition);
        }catch (Exception e){
            e.printStackTrace();
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription(e.getMessage());
        }
        return result;
    }
}
