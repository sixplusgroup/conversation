package finley.gmair.service.impl;

import finley.gmair.model.tmallGenie.*;
import finley.gmair.service.TmallDiscoveryService;
import finley.gmair.util.GoodsProperties;
import finley.gmair.util.ResponseCode;
import finley.gmair.util.ResultData;
import finley.gmair.util.tmall.TmallDeviceTypeEnum;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class TmallDiscoveryServiceImpl implements TmallDiscoveryService {

    static final String ACTION_SUFFIX = ".actions";

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

            // needed:deviceId,deviceName,deviceType,zone(可选),brand,model,icon,properties,actions,extensions(可选)
            Device device = new Device();

            // deviceId
            String deviceId = machineInfo.get("codeValue").toString();
            device.setDeviceId(deviceId);

            // deviceName,deviceType
            String goodsName = machineInfo.get("goodsName").toString();
            // https://open.bot.tmall.com/oauth/api/aliaslist（key为品类，value为该品类的别名取值枚举）
            String deviceName;
            // http://doc-bot.tmall.com/docs/doc.htm?spm=0.0.0.0.AELDhC&treeId=393&articleId=108271&docType=1中返回对应的英文值
            // https://www.yuque.com/qw5nze/ga14hc/gxhx67最新文档
            String deviceType;
            if (GoodsProperties.contains(String.valueOf(TmallDeviceTypeEnum.VMC), goodsName)) {
                deviceName = "新风机";
                deviceType = String.valueOf(TmallDeviceTypeEnum.VMC);
            } else if(GoodsProperties.contains(String.valueOf(TmallDeviceTypeEnum.fan), goodsName)) {
                deviceName = "风扇";
                deviceType = String.valueOf(TmallDeviceTypeEnum.fan);
            } else {
                // 未知类型的设备
                continue;
            }
            device.setDeviceName(deviceName);
            device.setDeviceType(deviceType);

            // actions
            List<String> actions = Arrays.asList(Objects.requireNonNull(
                    GoodsProperties.getValue(deviceType.concat(ACTION_SUFFIX))).split(","));
            device.setActions(actions);

            // zone(可选)
            // 参考接口： https://open.bot.tmall.com/oauth/api/placelist
//            device.setZone("客厅");

            // brand
            device.setBrand("果麦");

            // model
            String modelName = machineInfo.get("modelName").toString();
            device.setModel(modelName);

            // icon
            // 必填，否则会出现设备不显示的情况
            // 产品icon(https协议的url链接),像素最好160*160以免在app显示模糊
            String modelThumbnail = machineInfo.get("modelThumbnail").toString();
            device.setIcon(modelThumbnail);

            // properties
            // 与设备状态查询相关
            List<Attribute> properties = new ArrayList<>();
            device.setProperties(properties);

            // extensions(可选)
//            Extensions extensions = new Extensions("", "");
//            device.setExtensions(extensions);

            devices.add(device);
        }

        response.setPayload(new Payload(devices));
        return response;
    }

}
