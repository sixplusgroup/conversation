package finley.gmair.dao.impl;

import feign.Param;
import finley.gmair.dao.BaseDao;
import finley.gmair.dao.ModelFilterDao;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author zm
 * @date 2020/12/23 15:48
 * @description ModelFilterDaoImpl
 **/
@Repository
public class ModelFilterDaoImpl extends BaseDao implements ModelFilterDao {

    @Override
    public List<String> selectFilerIdByModelId(String modelId) {

        Map<String, Object> map = new HashMap<>();
        map.put("modelId", modelId);

        List<String> filterLinks = sqlSession.selectList(
                "gmair.machine.modelfilter.selectFilerIdByModelId", map);
        return filterLinks;
    }
}
