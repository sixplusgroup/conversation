package finley.gmair.dao.impl;

import finley.gmair.dao.BaseDao;
import finley.gmair.dao.FilterDao;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;

/**
 * @author zm
 * @date 2020/12/23 16:46
 * @description FilterDaoImpl
 **/
@Repository
public class FilterDaoImpl extends BaseDao implements FilterDao {
    @Override
    public String selectLinkById(String filterId) {
        Map<String, Object> map = new HashMap<>();
        map.put("filterId", filterId);
        return sqlSession.selectOne("gmair.machine.filter.selectLinkById", map);
    }
}
