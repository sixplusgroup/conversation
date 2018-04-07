package finley.gmair.controller;

import finley.gmair.model.wechat.ArticleTemplate;
import finley.gmair.model.wechat.PictureTemplate;
import finley.gmair.model.wechat.TextTemplate;
import finley.gmair.service.ArticleTemplateService;
import finley.gmair.service.PictureTemplateService;
import finley.gmair.service.TextTemplateService;
import finley.gmair.util.ResponseCode;
import finley.gmair.util.ResultData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/wechat/template")
public class TemplateController {
    private Logger logger = LoggerFactory.getLogger(TemplateController.class);

    @Autowired
    private ArticleTemplateService articleTemplateService;

    @Autowired
    private PictureTemplateService pictureTemplateService;

    @Autowired
    private TextTemplateService textTemplateService;

    @RequestMapping(value = "/article/create", method = RequestMethod.POST)
    public ResultData createArticle(ArticleTemplate template) {
        ResultData result = new ResultData();
        ResultData response = articleTemplateService.create(template);
        if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
            result.setResponseCode(ResponseCode.RESPONSE_OK);
            result.setData(response.getData());
        } else {
            result.setResponseCode(response.getResponseCode());
            result.setDescription(response.getDescription());
        }
        return result;
    }

    @RequestMapping(value = "/article/list", method = RequestMethod.GET)
    public ResultData queryArticle() {
        ResultData result = new ResultData();
        Map<String, Object> condition = new HashMap<>();
        condition.put("blockFlag", false);
        ResultData response = articleTemplateService.fetch(condition);
        if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
            result.setResponseCode(ResponseCode.RESPONSE_OK);
            result.setData(response.getData());
        }
        if (response.getResponseCode() == ResponseCode.RESPONSE_NULL) {
            result.setResponseCode(ResponseCode.RESPONSE_NULL);
            result.setDescription(response.getDescription());
        }
        if (response.getResponseCode() == ResponseCode.RESPONSE_ERROR) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription(response.getDescription());
        }
        return result;
    }

    @RequestMapping(value = "/article/update", method = RequestMethod.POST)
    public ResultData updateArticle(ArticleTemplate template) {
        ResultData result = new ResultData();
        ResultData response = articleTemplateService.modify(template);
        if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
            result.setResponseCode(ResponseCode.RESPONSE_OK);
            result.setData(response.getData());
        } else {
            result.setResponseCode(response.getResponseCode());
            result.setDescription(response.getDescription());
        }
        return result;
    }

    @RequestMapping(value = "/picture/create", method = RequestMethod.POST)
    public ResultData createPicture(PictureTemplate template) {
        ResultData result = new ResultData();
        ResultData response = pictureTemplateService.create(template);
        if (response.getResponseCode() != ResponseCode.RESPONSE_OK) {
            result.setResponseCode(response.getResponseCode());
            result.setDescription(response.getDescription());
        } else {
            result.setResponseCode(ResponseCode.RESPONSE_OK);
            result.setData(response.getData());
        }
        return result;
    }

    @RequestMapping(value = "/picture/list", method = RequestMethod.GET)
    public ResultData queryPicture() {
        ResultData result = new ResultData();
        Map<String, Object> condition = new HashMap<>();
        condition.put("blockFlag", false);
        ResultData response = pictureTemplateService.fetch(condition);
        if (response.getResponseCode() == ResponseCode.RESPONSE_NULL) {
            result.setResponseCode(ResponseCode.RESPONSE_NULL);
            result.setDescription(response.getDescription());
        } else if (response.getResponseCode() == ResponseCode.RESPONSE_ERROR) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription(response.getDescription());
        } else {
            result.setResponseCode(ResponseCode.RESPONSE_OK);
            result.setData(response.getData());
        }
        return result;
    }

    @RequestMapping(value = "/picture/update", method = RequestMethod.POST)
    public ResultData updatePicture(PictureTemplate template) {
        ResultData result = new ResultData();
        ResultData response = pictureTemplateService.modify(template);
        if (response.getResponseCode() != ResponseCode.RESPONSE_OK) {
            result.setResponseCode(response.getResponseCode());
            result.setDescription(response.getDescription());
        } else {
            result.setResponseCode(ResponseCode.RESPONSE_OK);
            result.setData(response.getData());
        }
        return result;
    }

    @RequestMapping(value = "/text/create", method = RequestMethod.POST)
    public ResultData createText(TextTemplate template) {
        ResultData result = new ResultData();
        ResultData response = textTemplateService.create(template);
        if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
            result.setResponseCode(ResponseCode.RESPONSE_OK);
            result.setData(response.getData());
        } else {
            result.setResponseCode(response.getResponseCode());
            result.setDescription(response.getDescription());
        }
        return result;
    }

    @RequestMapping(value = "/text/list", method = RequestMethod.GET)
    public ResultData queryText() {
        ResultData result = new ResultData();
        Map<String, Object> condition = new HashMap<>();
        condition.put("blockFlag", false);
        ResultData response = textTemplateService.fetch(condition);
        if (response.getResponseCode() == ResponseCode.RESPONSE_NULL) {
            result.setResponseCode(ResponseCode.RESPONSE_NULL);
            result.setDescription(response.getDescription());
        } else if (response.getResponseCode() == ResponseCode.RESPONSE_ERROR) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription(response.getDescription());
        } else {
            result.setResponseCode(ResponseCode.RESPONSE_OK);
            result.setData(response.getData());
        }
        return result;
    }

    @RequestMapping(value = "/text/update", method = RequestMethod.POST)
    public ResultData updateText(TextTemplate template) {
        ResultData result = new ResultData();
        ResultData response = textTemplateService.modify(template);
        if (response.getResponseCode() != ResponseCode.RESPONSE_OK) {
            result.setResponseCode(response.getResponseCode());
            result.setDescription(response.getDescription());
        } else {
            result.setResponseCode(ResponseCode.RESPONSE_OK);
            result.setData(response.getData());
        }
        return result;
    }
}