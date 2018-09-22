package finley.gmair.service.impl;

import finley.gmair.model.drift.Goods;
import finley.gmair.service.GoodsService;
import finley.gmair.util.ResultData;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * @author shenghaohu@hshvip.com
 * @date 2018/9/22
 */
@Service
public class GoodsServiceImpl implements GoodsService {
    @Override
    public ResultData fetchGoods(Map<String, Object> condition) {
        return null;
    }

    @Override
    public ResultData createGoods(Goods goods) {
        return null;
    }

    @Override
    public ResultData modifyGoods(Map<String, Object> condition) {
        return null;
    }
}