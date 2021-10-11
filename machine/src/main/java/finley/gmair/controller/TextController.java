package finley.gmair.controller;

import finley.gmair.service.*;
import finley.gmair.util.ResponseCode;
import finley.gmair.util.ResultData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * @author: Bright Chan
 * @date: 2020/7/24 16:34
 * @description: 设备高效滤网控制类
 */

@RestController
@RequestMapping("/machine/text")
public class TextController {

    @Autowired
    private TextService textService;

    /**
     *
     * @description:根据文字类型得到文字内容
     * @param: [textType]
     * @return: finley.gmair.util.ResultData
     * @auther: CK
     * @date: 2020/11/2 11:41
     */
    @GetMapping("")
    public ResultData getText(String textType) {
        ResultData res = new ResultData();
        Map<String, Object> condition = new HashMap<>();

        if (textType == null || textType.isEmpty()){
            res.setResponseCode(ResponseCode.RESPONSE_ERROR);
        }
        condition.put("textType",textType);
        condition.put("blockFlag",false);
        ResultData response = textService.fetch(condition);
        res = response;
        return res;
    }

}
