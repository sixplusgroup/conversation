package finley.gmair.service.impl;

import finley.gmair.model.tmallGenie.*;
import finley.gmair.service.TmallDiscoveryService;
import finley.gmair.util.ResponseCode;
import finley.gmair.util.ResultData;
import finley.gmair.util.tmall.TmallDeviceTypeEnum;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;

@Service
public class TmallDiscoveryServiceImpl implements TmallDiscoveryService {

    @Override
    @SuppressWarnings("unchecked")
    public AliGenieRe discovery(ResultData resultData, Header header) {
        AliGenieRe response = new AliGenieRe();
        header.setName(header.getName().concat("Response"));
        response.setHeader(header);

        List<Device> devices = new ArrayList<>();
        if (resultData.getResponseCode() != ResponseCode.RESPONSE_OK) {
            response.setPayload(new Payload(devices));
            return response;
        }

        List<LinkedHashMap<String, Object>> machineLists = (List<LinkedHashMap<String, Object>>) resultData.getData();
        for (LinkedHashMap<String, Object> machineInfo : machineLists) {

            Device device = new Device();
            device.setDeviceId(machineInfo.get("codeValue").toString());
            String goodsName = machineInfo.get("goodsName").toString();
            if (goodsName.equals("果麦新风机")) {
                // https://open.bot.tmall.com/oauth/api/aliaslist（key为品类，value为该品类的别名取值枚举）
                device.setDeviceName("新风机");
                // http://doc-bot.tmall.com/docs/doc.htm?spm=0.0.0.0.AELDhC&treeId=393&articleId=108271&docType=1中返回对应的英文值
                // https://www.yuque.com/qw5nze/ga14hc/gxhx67最新文档
                device.setDeviceType(String.valueOf(TmallDeviceTypeEnum.VMC));
                List<String> actions = Arrays.asList("TurnOn", "TurnOff");
                device.setActions(actions);
            } else if(goodsName.equals("果麦冷暖风扇")) {
                device.setDeviceName("风扇");
                device.setDeviceType(String.valueOf(TmallDeviceTypeEnum.fan));
                List<String> actions = Arrays.asList("TurnOn", "TurnOff", "SetWindSpeed", "AdjustUpWindSpeed",
                    "AdjustDownVolume", "OpenSwing", "CloseSwing");
                device.setActions(actions);
            }

            // 参考接口： https://open.bot.tmall.com/oauth/api/placelist
//            device.setZone("客厅");

            device.setBrand("果麦");

            // TODO 根据modelId获取型号
            String modelId = machineInfo.get("modelId").toString();
            device.setModel(modelId);

            // TODO 必填，否则会出现设备不显示的情况
            // icon链接是https以及大小是160*160
            device.setIcon("https://git.cn-hangzhou.oss-cdn.aliyun-inc.com/uploads/aicloud/aicloud-proxy-service/41baa00903a71c97e3533cf4e19a88bb/image.png");

            List<Attribute> properties = new ArrayList<>();
            device.setProperties(properties);

            Extensions extensions = new Extensions("", "");
            device.setExtensions(extensions);

            devices.add(device);
        }

        response.setPayload(new Payload(devices));
        return response;
    }

}
