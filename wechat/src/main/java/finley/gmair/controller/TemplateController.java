package finley.gmair.controller;

import finley.gmair.model.wechat.ArticleTemplate;
import finley.gmair.model.wechat.PictureTemplate;
import finley.gmair.model.wechat.TextTemplate;
import finley.gmair.service.ArticleTemplateService;
import finley.gmair.service.PictureTemplateService;
import finley.gmair.service.TextTemplateService;
import finley.gmair.util.ResponseCode;
import finley.gmair.util.ResultData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;


@RestController
@RequestMapping("/wechat/template")
public class TemplateController {
    @Autowired
    private TextTemplateService textTemplateService;
    @Autowired
    private ArticleTemplateService articleTemplateService;
    @Autowired
    private PictureTemplateService pictureTemplateService;

    @RequestMapping(method = RequestMethod.GET, value = "/text/list")
    public ResultData TextList() {
        ResultData result = new ResultData();
        Map<String, Object> condition = new HashMap<>();
        condition.put("blockFlag", false);
        ResultData response = textTemplateService.fetch(condition);
        if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
            result.setResponseCode(ResponseCode.RESPONSE_OK);
            result.setData(response.getData());
        }
        if (response.getResponseCode() == ResponseCode.RESPONSE_NULL) {
            result.setResponseCode(ResponseCode.RESPONSE_NULL);
            result.setDescription("text list is empty");
        }
        if (response.getResponseCode() == ResponseCode.RESPONSE_ERROR) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("text fetch error, please inspect");
        }
        return result;
    }

    @RequestMapping(method = RequestMethod.POST, value = "/text/create")
    public ResultData createText(TextTemplate template) {
        ResultData result = new ResultData();
        ResultData response = textTemplateService.create(template);
        if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
            result.setResponseCode(ResponseCode.RESPONSE_OK);
            result.setData(response.getData());
        }
        if (response.getResponseCode() == ResponseCode.RESPONSE_ERROR) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("text create error");
        }
        return result;
    }

    @RequestMapping(method = RequestMethod.POST, value = "/text/update")
    public ResultData updateText(TextTemplate template) {
        ResultData result = new ResultData();
        ResultData response = textTemplateService.modify(template);
        if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
            result.setResponseCode(ResponseCode.RESPONSE_OK);
            result.setData(response.getData());
        }
        if (response.getResponseCode() == ResponseCode.RESPONSE_ERROR) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("text update error");
        }
        if (response.getResponseCode() == ResponseCode.RESPONSE_NULL) {
            result.setResponseCode(ResponseCode.RESPONSE_NULL);
            result.setDescription("can't update with no such text");
        }
        return result;
    }

    @RequestMapping(method = RequestMethod.GET, value = "/textReply/query")
    public ResultData queryTextReply() {
        ResultData result = new ResultData();
        Map<String, Object> condition = new HashMap<>();
        condition.put("messageType", "text");
        ResultData response = textTemplateService.fetchTextReply(condition);
        if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
            result.setResponseCode(ResponseCode.RESPONSE_OK);
            result.setData(response.getData());
        }
        if (response.getResponseCode() == ResponseCode.RESPONSE_NULL) {
            result.setResponseCode(ResponseCode.RESPONSE_NULL);
            result.setDescription("reply is empty");
        }
        if (response.getResponseCode() == ResponseCode.RESPONSE_ERROR) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("Error: please try again");
        }
        return result;
    }

    @RequestMapping(method = RequestMethod.GET, value = "/article/list")
    public ResultData articleList() {
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
            result.setDescription("article is empty");
        }
        if (response.getResponseCode() == ResponseCode.RESPONSE_ERROR) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("article fetch error, please inspect");
        }
        return result;
    }

    @RequestMapping(method = RequestMethod.GET, value = "/articleReply/query")
    public ResultData queryArticleReply() {
        ResultData result = new ResultData();
        Map<String, Object> condition = new HashMap<>();
        condition.put("messageType", "text");
        ResultData response = articleTemplateService.fetchArticleReply(condition);
        if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
            result.setResponseCode(ResponseCode.RESPONSE_OK);
            result.setData(response.getData());
        }
        if (response.getResponseCode() == ResponseCode.RESPONSE_NULL) {
            result.setResponseCode(ResponseCode.RESPONSE_NULL);
            result.setDescription("reply is null");
        }
        if (response.getResponseCode() == ResponseCode.RESPONSE_ERROR) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("Error: please check up");
        }
        return result;
    }

    @RequestMapping(method = RequestMethod.POST, value = "/article/create")
    public ResultData createArticle(ArticleTemplate template) {
        ResultData result = new ResultData();
        ResultData response = articleTemplateService.create(template);
        if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
            result.setResponseCode(ResponseCode.RESPONSE_OK);
            result.setData(response.getData());
        }
        if (response.getResponseCode() == ResponseCode.RESPONSE_ERROR) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("article create error");
        }
        return result;
    }

    @PostMapping(value = "/article/update")
    public ResultData updateArticle(ArticleTemplate template) {
        ResultData result = new ResultData();
        ResultData response = articleTemplateService.modify(template);
        if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
            result.setResponseCode(ResponseCode.RESPONSE_OK);
            result.setData(response.getData());
        }
        if (response.getResponseCode() == ResponseCode.RESPONSE_ERROR) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("article update error");
        }
        if (response.getResponseCode() == ResponseCode.RESPONSE_NULL) {
            result.setResponseCode(ResponseCode.RESPONSE_NULL);
            result.setDescription("Error: can't update with no such article");
        }
        return result;
    }

    @GetMapping(value = "/picture/list")
    public ResultData pictureList() {
        ResultData result = new ResultData();
        Map<String, Object> condition = new HashMap<>();
        condition.put("blockFlag", false);
        ResultData response = pictureTemplateService.fetch(condition);
        if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
            result.setResponseCode(ResponseCode.RESPONSE_OK);
            result.setData(response.getData());
        }
        if (response.getResponseCode() == ResponseCode.RESPONSE_NULL) {
            result.setResponseCode(ResponseCode.RESPONSE_NULL);
            result.setDescription("picture is empty");
        }
        if (response.getResponseCode() == ResponseCode.RESPONSE_ERROR) {
            result.setResponseCode(response.getResponseCode());
            result.setDescription("picture fetch error,please inspect");
        }
        return result;
    }

    @GetMapping(value = "/pictureReply/query")
    public ResultData queryPictureReply() {
        ResultData result = new ResultData();
        Map<String, Object> condition = new HashMap<>();
        condition.put("keyWord","pic");
        ResultData response = pictureTemplateService.fetchPictureReply(condition);
        if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
            result.setResponseCode(ResponseCode.RESPONSE_OK);
            result.setData(response.getData());
        }
        if (response.getResponseCode() == ResponseCode.RESPONSE_NULL) {
            result.setResponseCode(ResponseCode.RESPONSE_NULL);
            result.setDescription("reply list is null");
        }
        if (response.getResponseCode() == ResponseCode.RESPONSE_ERROR) {
            result.setResponseCode(response.getResponseCode());
            result.setDescription("Reply error,please refresh");
        }
        return result;
    }

    @PostMapping(value = "/picture/create")
    public ResultData createPicture(PictureTemplate template) {
        ResultData result = new ResultData();
        ResultData response = pictureTemplateService.create(template);
        if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
            result.setResponseCode(ResponseCode.RESPONSE_OK);
            result.setData(response.getData());
        }
        if (response.getResponseCode() == ResponseCode.RESPONSE_ERROR) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("picture create error");
        }
        return result;
    }

    @PostMapping(value = "/picture/update")
    public ResultData updatePicture(PictureTemplate template) {
        ResultData result = new ResultData();
        ResultData response = pictureTemplateService.modify(template);
        if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
            result.setResponseCode(ResponseCode.RESPONSE_OK);
            result.setData(response.getData());
        }
        if (response.getResponseCode() == ResponseCode.RESPONSE_ERROR) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("picture update error");
        }
        if (response.getResponseCode() == ResponseCode.RESPONSE_NULL) {
            result.setResponseCode(ResponseCode.RESPONSE_NULL);
            result.setDescription("Error: can't update with no such picture");
        }
        return result;
    }
}