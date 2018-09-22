package finley.gmair.controller;

import finley.gmair.form.goods.GoodsForm;
import finley.gmair.util.ResultData;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/assemble/goods")
public class GoodsController {

    @RequestMapping("/create")
    public ResultData createDriftGoods(GoodsForm form) {
        ResultData result = new ResultData();

        return result;
    }
}
