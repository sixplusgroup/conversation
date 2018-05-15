package finley.gmair.controller;

import finley.gmair.service.PicService;
import finley.gmair.util.ResponseCode;
import finley.gmair.util.ResultData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/installation/pic")
public class PicController {
    @Autowired
    PicService picService;

    //从数据表install_pic中拉取重复图片的信息
    @RequestMapping(method = RequestMethod.GET, value = "copy")
    public ResultData copy() {
        ResultData result = new ResultData();

        Map<String, Object> condition = new HashMap<>();
        condition.put("copyFlag", true);
        condition.put("blockFlag", false);
        ResultData response = picService.fetchPic(condition);
        if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
            result.setResponseCode(ResponseCode.RESPONSE_OK);
            result.setData(response.getData());
            result.setDescription("success to duplicate pic");
        } else if (response.getResponseCode() == ResponseCode.RESPONSE_NULL) {
            result.setResponseCode(ResponseCode.RESPONSE_NULL);
            result.setDescription("no duplicate pic found");
        } else if (response.getResponseCode() == ResponseCode.RESPONSE_ERROR) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("server is busy now,please try again later.");
        }
        return result;
    }
}
