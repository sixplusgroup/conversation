package finley.gmair.controller;

import finley.gmair.form.goods.GoodsForm;
import finley.gmair.model.drift.Goods;
import finley.gmair.service.GoodsService;
import finley.gmair.util.ResponseCode;
import finley.gmair.util.ResultData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/assemble/goods")
public class GoodsController {

    @Autowired
    private GoodsService goodsService;

    /**
     * This method is used to create goods
     *
     * @return
     */
    @RequestMapping(method = RequestMethod.POST, value = "/create")
    public ResultData createDriftGoods(GoodsForm form) {
        ResultData result = new ResultData();

        //judge the parameter complete or not
        if (StringUtils.isEmpty(form.getGoodsName()) || StringUtils.isEmpty(form.getGoodsDescription()) ||
                StringUtils.isEmpty(form.getGoodsPrice())) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("Please make sure you fill all the required fields");
            return result;
        }

        //judge the goods whether exists or not
        Map<String, Object> condition = new HashMap<>();
        condition.put("goodsName", form.getGoodsName());
        ResultData response = goodsService.fetchGoods(condition);
        if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("The goods already exists");
            return result;
        }

        //build goods entity, insert goods
        String goodsName = form.getGoodsName().trim();
        String goodsDescription = form.getGoodsDescription().trim();
        double goodsPrice = form.getGoodsPrice();
        Goods goods = new Goods(goodsName, goodsDescription, goodsPrice);
        response = goodsService.createGoods(goods);
        if (response.getResponseCode() == ResponseCode.RESPONSE_ERROR) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("Fail to store drift goods to database");
        }
        if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
            result.setResponseCode(ResponseCode.RESPONSE_OK);
            result.setData(response.getData());
        }
        return result;
    }

    /**
     * This method is used to select goods list
     *
     * @return
     */
    @GetMapping(value = "/list")
    public ResultData getGoods() {
        ResultData result = new ResultData();
        Map<String, Object> condition = new HashMap<>();
        condition.put("blockFlag", false);
        ResultData response = goodsService.fetchGoods(condition);
        if (response.getResponseCode() == ResponseCode.RESPONSE_NULL) {
            result.setResponseCode(ResponseCode.RESPONSE_NULL);
            result.setDescription("No goods found");
        }
        if (response.getResponseCode() == ResponseCode.RESPONSE_ERROR) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("Query error, try again later");
        }
        if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
            result.setResponseCode(ResponseCode.RESPONSE_OK);
            result.setData(response.getData());
        }
        return result;
    }
}
