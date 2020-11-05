package finley.gmair.service.impl;

import finley.gmair.dao.SkuItemMapper;
import finley.gmair.service.SkuItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * @author ：tsl
 * @date ：Created in 2020/11/5 21:51
 * @description ：
 */

@Service
public class SkuItemServiceImpl implements SkuItemService {
    @Autowired
    SkuItemMapper skuItemMapper;

    @Override
    public String selectMachineModelByNumIidAndSkuId(String numIid, String skuId) {
        List<String> modelList = skuItemMapper.selectMachineModelByNumIidAndSkuId(numIid, skuId);
        if (CollectionUtils.isEmpty(modelList)) {
            modelList = skuItemMapper.selectMachineModelByNumIid(numIid);
        }
        return CollectionUtils.isEmpty(modelList) ? "" : modelList.get(0);
    }
}
