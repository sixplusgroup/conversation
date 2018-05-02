package finley.gmair.controller;

import finley.gmair.form.wechat.ArticleTemplateForm;
import finley.gmair.form.wechat.PictureTemplateForm;
import finley.gmair.form.wechat.TextTemplateForm;
import finley.gmair.model.wechat.*;
import finley.gmair.service.ArticleTemplateService;
import finley.gmair.service.AutoReplyService;
import finley.gmair.service.PictureTemplateService;
import finley.gmair.service.TextTemplateService;
import finley.gmair.util.ResponseCode;
import finley.gmair.util.ResultData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;


@RestController
@RequestMapping("/wechat/template")
public class TemplateController {
    private final static String TEMPLATE_TEXT = "text";

    @Autowired
    private TextTemplateService textTemplateService;

    @Autowired
    private ArticleTemplateService articleTemplateService;

    @Autowired
    private PictureTemplateService pictureTemplateService;

    @Autowired
    private AutoReplyService autoReplyService;

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

    /**
     * This method is called by the portal to create a template of text type
     *
     * @param form
     * @return
     */
    @RequestMapping(method = RequestMethod.POST, value = "/text/create")
    public ResultData createText(TextTemplateForm form) {
        ResultData result = new ResultData();
        if (StringUtils.isEmpty(form.getInMessageType()) || StringUtils.isEmpty(form.getKeyword()) || StringUtils.isEmpty(form.getResponse())) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("please make sure you fill all the required fields");
            return result;
        }
        //save the template and then bind it to keyword in auto reply
        TextTemplate template = new TextTemplate(TEMPLATE_TEXT, form.getResponse());
        ResultData response = textTemplateService.create(template);
        if (response.getResponseCode() == ResponseCode.RESPONSE_ERROR) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("text create error");
            return result;
        }
        //bind the keyword and template
        template = (TextTemplate) response.getData();
        AutoReply reply = new AutoReply(form.getInMessageType(), form.getKeyword(), template.getTemplateId());
        response = autoReplyService.create(reply);
        if (response.getResponseCode() == ResponseCode.RESPONSE_ERROR) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("Fail to save the auto reply");
        }
        if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
            result.setResponseCode(ResponseCode.RESPONSE_OK);
            result.setDescription("The auto reply is configured successfully");
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
        ResultData response = textTemplateService.fetch(condition);
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
    public ResultData createArticle(ArticleTemplateForm form) {
        ResultData result = new ResultData();
        if (StringUtils.isEmpty(form.getInMessageType()) || StringUtils.isEmpty(form.getArticleTitle()) || StringUtils.isEmpty(form.getDescriptionType()) || StringUtils.isEmpty(form.getKeyword())
                || StringUtils.isEmpty(form.getDescriptionContent()) || StringUtils.isEmpty(form.getPictureUrl()) || StringUtils.isEmpty(form.getArticleUrl())) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("please make sure you fill all the required fields");
            return result;
        }

        ArticleTemplate template = new ArticleTemplate(form.getArticleTitle(), form.getDescriptionType(), form.getDescriptionContent(), form.getPictureUrl(), form.getArticleUrl());
        ResultData response = articleTemplateService.create(template);
        if (response.getResponseCode() == ResponseCode.RESPONSE_ERROR) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("article create error");
            return result;
        }

        template = (ArticleTemplate) response.getData();
        AutoReply reply = new AutoReply(form.getInMessageType(), form.getKeyword(), template.getTemplateId());
        response = autoReplyService.create(reply);
        if (response.getResponseCode() == ResponseCode.RESPONSE_ERROR) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("Fail to save the auto reply");
        }
        if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
            result.setResponseCode(ResponseCode.RESPONSE_OK);
            result.setDescription("The auto reply is configured successfully");
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
        condition.put("keyWord", "pic");
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
    public ResultData createPicture(PictureTemplateForm form) {
        ResultData result = new ResultData();
        if (StringUtils.isEmpty(form.getInMessageType()) || StringUtils.isEmpty(form.getKeyword()) || StringUtils.isEmpty(form.getPictureUrl())) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("please make sure you fill all the required fields");
            return result;
        }

        PictureTemplate template = new PictureTemplate(form.getPictureUrl());
        ResultData response = pictureTemplateService.create(template);
        if (response.getResponseCode() == ResponseCode.RESPONSE_ERROR) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("picture create error");
            return result;
        }

        template = (PictureTemplate) response.getData();
        AutoReply reply = new AutoReply(form.getInMessageType(), form.getKeyword(), template.getTemplateId());
        response = autoReplyService.create(reply);
        if (response.getResponseCode() == ResponseCode.RESPONSE_ERROR) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("Fail to save the auto reply");
        }
        if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
            result.setResponseCode(ResponseCode.RESPONSE_OK);
            result.setDescription("The auto reply is configured successfully");
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