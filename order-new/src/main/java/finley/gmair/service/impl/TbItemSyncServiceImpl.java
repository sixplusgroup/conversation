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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
    public void fullImport() {
        List<TbUser> tbUserList = tbUserMapper.selectAll();
        for (TbUser tbUser : tbUserList) {
            String sessionKey = tbUser.getSessionKey();
            ItemsOnsaleGetRequest request = new ItemsOnsaleGetRequest();
            request.setFields(IMPORT_FIELDS);
            ItemsOnsaleGetResponse response = tbAPIServiceImpl.itemsOnSaleGet(request, sessionKey);
            List<Item> itemList = response.getItems();
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
                    System.out.println(skuItem.toString());
                    skuItemMapper.insert(skuItem);
                } else {
                    for (Sku sku : completeItem.getSkus()) {
                        SkuItem skuItem = new SkuItem();
                        skuItem.setItemId(IDGenerator.generate("SKU"));
                        skuItem.setNumIid(completeItem.getNumIid().toString());
                        skuItem.setTitle(completeItem.getTitle());
                        skuItem.setSkuId(sku.getSkuId().toString());
                        skuItem.setPrice(Double.parseDouble(sku.getPrice()));
                        skuItem.setPropertiesName(getCNProperties(sku.getPropertiesName()));
                        System.out.println(skuItem.toString());
                        skuItemMapper.insert(skuItem);
                    }
                }
            }
        }
    }

    /**
     * 得到中文销售属性
     *
     * @param propertiesStr pid1:vid1:pid_name1:vid_name1;pid2:vid2:pid_name2:vid_name2……
     * @return pid_name1:vid_name1;pid_name2:vid_name2……
     */
    private static String getCNProperties(String propertiesStr) {
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
