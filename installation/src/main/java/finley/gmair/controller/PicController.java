package finley.gmair.controller;

import finley.gmair.service.PicService;
import finley.gmair.util.ResponseCode;
import finley.gmair.util.ResultData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/installation/pic")
public class PicController {
    @Autowired
    PicService picService;

    //从数据表install_pic中拉取重复图片的信息
    @RequestMapping("copypic")
    public ResultData copypic(){
        ResultData result = new ResultData();

        Map<String ,Object> condition = new HashMap<>();
        condition.put("copyFlag",true);
        condition.put("blockFlag",false);
        ResultData response = picService.fetchPic(condition);
        if(response.getResponseCode() == ResponseCode.RESPONSE_OK)
        {
            result.setResponseCode(ResponseCode.RESPONSE_OK);
            result.setData(response.getData());
            result.setDescription("success to duplicate pic");
        }
        else if(response.getResponseCode() == ResponseCode.RESPONSE_NULL)
        {
            result.setResponseCode(ResponseCode.RESPONSE_NULL);
            result.setDescription("no duplicate pic found");
        }
        else if(response.getResponseCode() == ResponseCode.RESPONSE_ERROR)
        {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("server is busy now,please try again later.");
        }
        return result;
    }

    //处理上传图片的请求
    @RequestMapping(method = RequestMethod.POST, value = "/upload")
    public ResultData upload(@RequestParam MultipartFile file) {
        ResultData result = new ResultData();

        //String absolutePath = this.getClass().getClassLoader().getResource("").getPath();
        //int index = absolutePath.indexOf("/target/classes/");
        //String basePath = absolutePath.substring(0, index);
        ResultData response = picService.uploadPic(file);
        if (response.getResponseCode() == ResponseCode.RESPONSE_ERROR) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("服务器繁忙，请稍后再试!");
            return result;
        } else if (response.getResponseCode() == ResponseCode.RESPONSE_NULL) {
            result.setResponseCode(ResponseCode.RESPONSE_NULL);
            result.setDescription("文件为空");
            return result;
        } else {
            result.setData(response.getData());
            result.setDescription("文件上传成功");
            return result;
        }
    }
}
