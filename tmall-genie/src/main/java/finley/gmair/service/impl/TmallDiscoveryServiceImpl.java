package finley.gmair.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import finley.gmair.model.tmallGenie.*;
import finley.gmair.service.TmallDiscoveryService;
import finley.gmair.util.ResultData;
import finley.gmair.util.tmall.TmallDeviceType;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class TmallDiscoveryServiceImpl implements TmallDiscoveryService {

    @Override
    public Payload discovery(ResultData resultData) {
        JSONArray json = (JSONArray) resultData.getData();
        List<Device> devices = new ArrayList<>();
        for (Object object : json) {
            JSONObject o = (JSONObject) object;
            Device device = new Device();

            // 最长32个字符，不能有&、#等特殊符号，否则会出现乱码显示
            device.setDeviceId(o.getString("codeValue"));

            String goodsName = o.getString("goodsName");
            if (goodsName.equals("果麦新风机")) {
                // 参考接口： https://open.bot.tmall.com/oauth/api/aliaslist（key为品类，value为该品类的别名取值枚举）
                device.setDeviceName("新风机");
                // http://doc-bot.tmall.com/docs/doc.htm?spm=0.0.0.0.AELDhC&treeId=393&articleId=108271&docType=1中返回对应的英文值
                // https://www.yuque.com/qw5nze/ga14hc/gxhx67最新文档
                device.setDeviceType(String.valueOf(TmallDeviceType.VMC));
            }

            // 参考接口： https://open.bot.tmall.com/oauth/api/placelist
            device.setZone("客厅");

            device.setBrand("果麦");

            // TODO 根据modelId获取型号
            String modelId = o.getString("modelId");
            device.setModel("");

            // TODO 必填，否则会出现设备不显示的情况
            // icon链接是https以及大小是160*160
            device.setIcon("https://git.cn-hangzhou.oss-cdn.aliyun-inc.com/uploads/aicloud/aicloud-proxy-service/41baa00903a71c97e3533cf4e19a88bb/image.png");

            List<Attribute> properties = new ArrayList<>();
            device.setProperties(properties);

            List<String> actions = Arrays.asList("TurnOn", "TurnOff", "Query");
            device.setActions(actions);

            Extensions extensions = new Extensions("", "");
            device.setExtensions(extensions);

            devices.add(device);
        }

        return new Payload(devices);
    }

}
