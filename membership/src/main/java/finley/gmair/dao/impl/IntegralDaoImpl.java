package finley.gmair.dao.impl;

import finley.gmair.dao.BaseDao;
import finley.gmair.dao.IntegralDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;

/**
 * @ClassName: IntegralDaoImpl
 * @Description: TODO
 * @Author fan
 * @Date 2021/7/13 2:59 PM
 */

@Repository
public class IntegralDaoImpl extends BaseDao implements IntegralDao {
    private Logger logger = LoggerFactory.getLogger(IntegralDaoImpl.class);
}
