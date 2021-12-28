package finley.gmair.handler;

import com.alibaba.fastjson.JSON;
import com.jd.open.api.sdk.domain.ware.SkuReadService.response.searchSkuList.Sku;
import com.jd.open.api.sdk.domain.ware.WareReadService.response.searchWare4Valid.Ware;
import com.jd.open.api.sdk.request.ware.SkuReadSearchSkuListRequest;
import com.jd.open.api.sdk.request.ware.WareReadSearchWare4ValidRequest;
import com.jd.open.api.sdk.response.ware.SkuReadSearchSkuListResponse;
import com.jd.open.api.sdk.response.ware.WareReadSearchWare4ValidResponse;
import finley.gmair.api.JdApi;
import finley.gmair.converter.JdSkuItemConverter;
import finley.gmair.model.domain.ShopAuthorizeInfo;
import finley.gmair.model.domain.UnifiedSkuItem;
import finley.gmair.model.enums.PlatformEnum;
import finley.gmair.model.result.PullResult;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * @author ：tsl
 * @date ：Created in 2021-12-24 13:45
 * @description ：
 */

@Component
public class JdSkuItemPullHandler extends AbstractSkuItemPullHandler<Sku> implements InitializingBean {

    @Resource
    JdSkuItemConverter jdSkuItemConverter;

    @Override
    protected List<Sku> pull(ShopAuthorizeInfo authorizeInfo, PullResult result) {
        List<Ware> wareList = getAllValidProducts(authorizeInfo);
        List<Sku> res = new ArrayList<>();
        if (CollectionUtils.isEmpty(wareList)) {
            return res;
        }
        SkuReadSearchSkuListRequest request = new SkuReadSearchSkuListRequest();
        request.setField("wareId,skuId,status,wareTitle,skuName,jdPrice,saleAttrs,features,modified,created");
        for (Ware ware : wareList) {
            request.setWareId(String.valueOf(ware.getWareId()));
            try {
                JdApi jdApi = JdApi.setUp(authorizeInfo.getSessionKey(), authorizeInfo.getAppKey(), authorizeInfo.getAppSecret());
                SkuReadSearchSkuListResponse response = jdApi.execute(request);
                res.addAll(response.getPage().getData());
            } catch (Exception e) {
                e.printStackTrace();
                return res;
            }
        }
        return res;
    }

    private List<Ware> getAllValidProducts(ShopAuthorizeInfo authorizeInfo) {
        final String params = "wareId,title,shopId,wareStatus,itemNum,barCode,logo," +
                "created,modified,onlineTime,offlineTime," +
                "weight,length,width,height,features," +
                "marketPrice,costPrice,jdPrice,brandName,stockNum,multiCateProps";
        int curPage = 1;
        int pageSize = 50; // 最大就是50

        WareReadSearchWare4ValidRequest request = new WareReadSearchWare4ValidRequest();
        request.setPageSize(pageSize);
        request.setPageNo(curPage);
        request.setField(params);
        try {
            JdApi jdApi = JdApi.setUp(authorizeInfo.getSessionKey(), authorizeInfo.getAppKey(), authorizeInfo.getAppSecret());
            WareReadSearchWare4ValidResponse response = jdApi.execute(request);
            List<Ware> res = response.getPage().getData();
            while (response.getPage().getTotalItem() > (long) curPage * pageSize) {
                curPage++;
                request.setPageNo(curPage);
                response = jdApi.execute(request);
                res.addAll(response.getPage().getData());
            }
            return res;
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    @Override
    protected UnifiedSkuItem convert(Sku originalSkuItem) {
        return jdSkuItemConverter.jdSkuItem2UnifiedSkuItem(originalSkuItem);
    }

    @Override
    public void afterPropertiesSet() {
        SkuItemPullHandlerContext.registerHandler(getPlatform().getValue(), this);
    }

    @Override
    protected PlatformEnum getPlatform() {
        return PlatformEnum.JD;
    }
}
