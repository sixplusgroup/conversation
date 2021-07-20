package finley.gmair.service.impl;

import com.taobao.api.domain.Item;
import com.taobao.api.domain.Sku;
import com.taobao.api.request.ItemSellerGetRequest;
import com.taobao.api.request.ItemsOnsaleGetRequest;
import com.taobao.api.response.ItemSellerGetResponse;
import com.taobao.api.response.ItemsOnsaleGetResponse;
import finley.gmair.dao.SkuItemMapper;
import finley.gmair.dao.TbUserMapper;
import finley.gmair.model.ordernew.SkuItem;
import finley.gmair.model.ordernew.TbUser;
import finley.gmair.service.TbItemSyncService;
import finley.gmair.util.IDGenerator;
import finley.gmair.util.ResponseCode;
import finley.gmair.util.ResultData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author ：tsl
 * @date ：Created in 2020/10/19 23:23
 * @description ：
 */

@Service
public class TbItemSyncServiceImpl implements TbItemSyncService {

    private static final String IMPORT_FIELDS = "num_iid";

    private static final String IMPORT_FIELDS_COMPLETE = "num_iid,title,sku.sku_id,sku.price,sku.properties_name,price";

    @Autowired
    SkuItemMapper skuItemMapper;
    @Autowired
    TbUserMapper tbUserMapper;
    @Autowired
    TbAPIServiceImpl tbAPIServiceImpl;

    @Override
    public ResultData fullImport() {
        ResultData resultData = new ResultData();
        List<TbUser> tbUserList = tbUserMapper.selectAll();
        for (TbUser tbUser : tbUserList) {
            String sessionKey = tbUser.getSessionKey();
            ItemsOnsaleGetRequest request = new ItemsOnsaleGetRequest();
            request.setFields(IMPORT_FIELDS);
            ItemsOnsaleGetResponse response = tbAPIServiceImpl.itemsOnSaleGet(request, sessionKey);
            if (!response.isSuccess()) {
                resultData.setResponseCode(ResponseCode.RESPONSE_ERROR);
                resultData.setDescription("tbAPI itemsOnSaleGet error");
                return resultData;
            }
            List<Item> itemList = response.getItems();
            if (CollectionUtils.isEmpty(itemList)) {
                return resultData;
            }
            //通过API获取skuItemList
            List<SkuItem> skuItemList = new ArrayList<>();
            for (Item item : itemList) {
                Long numIid = item.getNumIid();
                ItemSellerGetRequest req = new ItemSellerGetRequest();
                req.setNumIid(numIid);
                req.setFields(IMPORT_FIELDS_COMPLETE);
                ItemSellerGetResponse itemSellerGetResponse = tbAPIServiceImpl.itemSellerGet(req, sessionKey);
                Item completeItem = itemSellerGetResponse.getItem();
                if (completeItem.getSkus() == null) {
                    SkuItem skuItem = new SkuItem();
                    skuItem.setItemId(IDGenerator.generate("SKU"));
                    skuItem.setNumIid(completeItem.getNumIid().toString());
                    skuItem.setTitle(completeItem.getTitle());
                    skuItem.setSkuId(null);
                    skuItem.setPrice(Double.parseDouble(completeItem.getPrice()));
                    skuItem.setPropertiesName(null);
                    skuItemList.add(skuItem);
                } else {
                    for (Sku sku : completeItem.getSkus()) {
                        SkuItem skuItem = new SkuItem();
                        skuItem.setItemId(IDGenerator.generate("SKU"));
                        skuItem.setNumIid(completeItem.getNumIid().toString());
                        skuItem.setTitle(completeItem.getTitle());
                        skuItem.setSkuId(sku.getSkuId().toString());
                        skuItem.setPrice(Double.parseDouble(sku.getPrice()));
                        skuItem.setPropertiesName(getCNProperties(sku.getPropertiesName()));
                        skuItemList.add(skuItem);
                    }
                }
            }
            for (SkuItem skuItem : skuItemList) {
                List<SkuItem> skuItemDbList = skuItemMapper.selectAllByNumIidAndSkuId(skuItem.getNumIid(), skuItem.getSkuId());
                //根据numIid和skuId判断是否存在，存在则替换字段：是否虚拟、商品的型号
                if (!CollectionUtils.isEmpty(skuItemDbList)) {
                    SkuItem skuItemDb = skuItemDbList.get(0);
                    //替换是否虚拟、商品型号
                    skuItem.setFictitious(skuItemDb.getFictitious());
                    skuItem.setMachineModel(skuItemDb.getMachineModel());
                }
            }
            //清除原数据
            skuItemMapper.truncateTable();
            //插入新数据
            for (SkuItem skuItem : skuItemList) {
                skuItemMapper.insert(skuItem);
            }
        }
        return resultData;
    }

    /**
     * 得到中文销售属性
     *
     * @param propertiesStr pid1:vid1:pid_name1:vid_name1;pid2:vid2:pid_name2:vid_name2……
     * @return pid_name1:vid_name1;pid_name2:vid_name2……
     */
    private String getCNProperties(String propertiesStr) {
        String[] properties = propertiesStr.split(";");
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < properties.length; i++) {
            String property = properties[i];
            int secondIndex = property.indexOf(":", property.indexOf(":") + 1);
            String cnProperty = property.substring(secondIndex + 1, property.length());
            sb.append(cnProperty);
            if (i < properties.length - 1) {
                sb.append(";");
            }
        }
        return sb.toString();
    }
}
