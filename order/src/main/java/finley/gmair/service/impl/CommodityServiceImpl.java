package finley.gmair.service.impl;

import finley.gmair.dao.CommodityDao;
import finley.gmair.service.CommodityService;
import finley.gmair.util.ResultData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;


@Service
public class CommodityServiceImpl implements CommodityService{

    @Autowired
    CommodityDao commodityDao;

    @Override
    public ResultData fetchCommodity(Map<String, Object> condition) {
        return commodityDao.queryCommodity(condition);
    }
}
