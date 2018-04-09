package finley.gmair.controller.message;

import finley.gmair.form.message.MessageTemplateForm;
import finley.gmair.model.message.MessageCatalog;
import finley.gmair.model.message.MessageTemplate;
import finley.gmair.util.ResponseCode;
import finley.gmair.util.ResultData;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/template")
public class TemplateController {
    @RequestMapping(method = RequestMethod.POST, value = "/message/create")
    public ResultData createMessageTemplate(MessageTemplateForm form) {
        ResultData result = new ResultData();
        if (StringUtils.isEmpty(form.getText())) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("Please fill the message template first");
            return result;
        }
        MessageCatalog catalog = MessageCatalog.AUTHENTICATION;
        //create the template
        MessageTemplate template = new MessageTemplate(catalog, form.getText());
        return result;
    }

    @RequestMapping(method = RequestMethod.GET, value = "/message")
    public ResultData fetchMessageTemplate() {
        ResultData result = new ResultData();

        return result;
    }

    @RequestMapping(method = RequestMethod.GET, value = "/message/authentication")
    public ResultData fetchAuthMessageTemplate() {
        ResultData result = new ResultData();
        Map<String, Object> condition = new HashMap<>();
        condition.put("catalog", MessageCatalog.AUTHENTICATION.getCode());
        condition.put("blockFlag", false);

        return result;
    }


}
