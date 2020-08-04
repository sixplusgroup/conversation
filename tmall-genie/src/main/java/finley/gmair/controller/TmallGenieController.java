package finley.gmair.controller;

import finley.gmair.model.tmallGenie.AliGenieRe;
import finley.gmair.model.tmallGenie.Header;
import finley.gmair.model.tmallGenie.Payload;
import finley.gmair.service.*;
import finley.gmair.util.ResponseCode;
import finley.gmair.util.ResultData;
import finley.gmair.util.tmall.TmallNameSpaceEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/tmallgenie")
public class TmallGenieController {

    private Logger logger = LoggerFactory.getLogger(TmallGenieController.class);

    @Autowired
    private TmallQueryService tmallQueryService;

    @Autowired
    private TmallControlService tmallControlService;

    @Autowired
    private TmallDiscoveryService tmallDiscoveryService;

    @Autowired
    private TmallUpdateService tmallUpdateService;

    @Autowired
    private ReceptionService receptionService;

    /**
     * @param request 请求体
     * @return 返回结果
     * @see <a href="https://www.yuque.com/qw5nze/ga14hc/rftwyo">AliGenie智能家居接入协议</a>
     */
    @PostMapping(value = "/voice/control")
    public AliGenieRe voiceControl(@RequestBody AliGenieRe request) {

        if (request == null) {
            logger.info("request is null");
            return null;
        }

        // 构建服务返回结果
        AliGenieRe response = new AliGenieRe();

        Header header = request.getHeader();
        Payload payload = request.getPayload();

        // 将messageID封装在设备发现协议中。会话ID，用于排查问题记日志
        logger.info("messageID: " + header.getMessageId());

        TmallNameSpaceEnum nameSpace = TmallNameSpaceEnum.fromString(header.getNamespace());
        switch (nameSpace) {
            case QUERY:
                logger.info("begin query...");
                response = tmallQueryService.query(payload, header);
                break;

            case CONTROL:
                logger.info("begin control...");
                response = tmallControlService.control(payload, header);
                break;

            case DISCOVERY:
                logger.info("begin discovery...");
                String accessToken = payload.getAccessToken();
                logger.info("accessToken: " + accessToken);

                ResultData resultData = receptionService.getDeviceListByToken(accessToken);

                logger.info(String.valueOf(resultData));

                // 根据结果做一些转换
                response = tmallDiscoveryService.discovery(resultData, header);

                if (response == null) {
                    logger.info("can not find any device");
                }

                break;

            default:
                break;
        }

        logger.info(String.valueOf(response));

        return response;
    }

    /**
     * 设备列表更新通知
     * https://open.taobao.com/api.htm?docId=42961&docType=2&scopeId=16015
     *
     * @param accessToken
     * @return
     */
    @PostMapping("/list/update")
    ResultData updateListNotify(@RequestParam String accessToken) {
        ResultData result = new ResultData();
        String authorizationToken = tmallUpdateService.getAuthorizationToken(accessToken);
        try {
            result = tmallUpdateService.updateListNotify(authorizationToken);
        } catch (IOException e) {
            logger.info("updateListNotify, Exception:", e);
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription(e.getMessage());
        }
        logger.info("updateListNotify result:{}", result);
        return result;
    }
}
