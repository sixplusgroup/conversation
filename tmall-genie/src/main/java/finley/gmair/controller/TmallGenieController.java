package finley.gmair.controller;

import finley.gmair.model.tmallGenie.AliGenieRe;
import finley.gmair.model.tmallGenie.Header;
import finley.gmair.model.tmallGenie.Payload;
import finley.gmair.service.MachineService;
import finley.gmair.service.TmallControlService;
import finley.gmair.service.TmallDiscoveryService;
import finley.gmair.service.TmallQueryService;
import finley.gmair.util.ResultData;

import finley.gmair.util.tmall.TmallNameSpaceEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
    private MachineService machineService;

    /**
     * @see <a href="https://www.yuque.com/qw5nze/ga14hc/rftwyo">AliGenie智能家居接入协议</a>
     * @param request 请求体
     * @return 返回结果
     */
    @PostMapping(value = "/voice/control")
    public AliGenieRe voiceControl(@RequestBody AliGenieRe request) {
        // 构建服务返回结果
        AliGenieRe response = new AliGenieRe();

        Header header = request.getHeader();
        Payload payload = request.getPayload();

        // 将messageID封装在设备发现协议中。会话ID，用于排查问题记日志
        logger.info("messageID: " + header.getMessageId());

        // 根据accessToken获取consumerId
        // 只要在请求中带上token，后台会自动把它映射称Spring Security里面的Principal对象，进而可以获取到用户的consumerid
//        SecurityContext context = SecurityContextHolder.getContext();
//        Authentication authentication = context.getAuthentication();
//        String consumerId = (String) authentication.getPrincipal();
        String consumerId = "CSR20181008hh2ufn48";

        TmallNameSpaceEnum nameSpace = TmallNameSpaceEnum.fromString(header.getNamespace());
        switch (nameSpace) {
            case QUERY:
                response = tmallQueryService.query(payload, header);
                break;

            case CONTROL:
                response = tmallControlService.control(payload, header);
                break;

            case DISCOVERY:
                ResultData resultData = machineService.obtainMachineList(consumerId);
                // 根据结果做一些转换
                response = tmallDiscoveryService.discovery(resultData, header);
                break;

            default:
                break;
        }

        return response;
    }

//    /**
//     * 测试连通性
//     *
//     * @return 测试结果
//     */
//    @GetMapping(value = "/test")
//    public ResultData test() {
//        // 构建服务返回结果
//        ResultData result = new ResultData();
//        String id = "test";
//        System.out.println(id);
//        result.setResponseCode(ResponseCode.RESPONSE_OK);
//        result.setData(id);
//        return result;
//    }

}
