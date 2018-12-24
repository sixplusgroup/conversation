package finley.gmair.controller;

import com.alibaba.fastjson.JSON;
import finley.gmair.service.CityAQIService;
import finley.gmair.util.ResultData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/airquality/city")
public class CityAQIController {
    private Logger logger = LoggerFactory.getLogger(CityAQIController.class);

    @Autowired
    private CityAQIService cityAQIService;

    /**
     * 调用该接口重新获取最新的空气质量数据
     *
     * @return
     */
    @PostMapping("/aqi/refresh")
    public ResultData refresh() {
        ResultData result = new ResultData();
        ResultData response = cityAQIService.obtain();

        logger.info(JSON.toJSONString(response));
        return result;
    }
}
