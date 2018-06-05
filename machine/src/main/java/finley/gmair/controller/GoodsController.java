package finley.gmair.controller;

import finley.gmair.form.goods.GoodsForm;
import finley.gmair.form.goods.ModelForm;
import finley.gmair.model.goods.Goods;
import finley.gmair.model.goods.GoodsModel;
import finley.gmair.service.GoodsService;
import finley.gmair.util.ResponseCode;
import finley.gmair.util.ResultData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * @author shenghaohu@hshvip.com
 * @date 2018/5/24
 */
@RestController
@RequestMapping("/machine/goods")
public class GoodsController {
    @Autowired
    private GoodsService goodsService;

    @GetMapping(value = "/list")
    public ResultData queryGoods() {
        ResultData result = new ResultData();
        Map<String, Object> condition = new HashMap<>();
        condition.put("blockFlag", false);
        ResultData response = goodsService.fetchGoods(condition);
        if (response.getResponseCode() == ResponseCode.RESPONSE_NULL) {
            result.setResponseCode(ResponseCode.RESPONSE_NULL);
            result.setDescription("goods list is empty");
        }
        if (response.getResponseCode() == ResponseCode.RESPONSE_ERROR) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("goods query error, please inspect");
        }
        if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
            result.setResponseCode(ResponseCode.RESPONSE_OK);
            result.setData(response.getData());
        }
        return result;
    }

    @PostMapping(value = "/create")
    public ResultData createGoods(GoodsForm goodsForm) {
        ResultData result = new ResultData();
        if (StringUtils.isEmpty(goodsForm.getGoodsName()) || StringUtils.isEmpty(goodsForm.getGoodsDescription())
                || StringUtils.isEmpty(goodsForm.getGoodsPrice())) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("please make sure you fill all the required fields");
            return result;
        }
        Goods goods = new Goods(goodsForm.getGoodsName(), goodsForm.getGoodsDescription(), goodsForm.getGoodsPrice());
        ResultData response = goodsService.createGoods(goods);
        if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
            result.setResponseCode(ResponseCode.RESPONSE_OK);
            result.setData(response.getData());
        } else {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("Fail to save goods");
        }
        return result;
    }

    @CrossOrigin
    @GetMapping(value = "/model/list")
    public ResultData queryModel() {
        ResultData result = new ResultData();
        Map<String, Object> condition = new HashMap<>();
        condition.put("blockFlag", false);
        ResultData response = goodsService.fetchModel(condition);
        if (response.getResponseCode() == ResponseCode.RESPONSE_NULL) {
            result.setResponseCode(ResponseCode.RESPONSE_NULL);
            result.setDescription("model is empty");
        }
        if (response.getResponseCode() == ResponseCode.RESPONSE_ERROR) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("model query error, please inspect");
        }
        if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
            result.setResponseCode(ResponseCode.RESPONSE_OK);
            result.setData(response.getData());
        }
        return result;
    }

    @PostMapping("/model/create")
    public ResultData createModel(ModelForm modelForm) {
        ResultData result = new ResultData();
        if (StringUtils.isEmpty(modelForm.getGoodsId()) || StringUtils.isEmpty(modelForm.getModelCode())
                || StringUtils.isEmpty(modelForm.getModelName())) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("please make sure you fill all the required fields");
            return result;
        }
        GoodsModel model = new GoodsModel(modelForm.getGoodsId(), modelForm.getModelCode(), modelForm.getModelName());
        ResultData response = goodsService.createModel(model);
        if (response.getResponseCode() != ResponseCode.RESPONSE_OK) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("Fail to save goodsModel");
        } else {
            result.setResponseCode(ResponseCode.RESPONSE_OK);
            result.setData(response.getData());
        }
        return result;
    }
}