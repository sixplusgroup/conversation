package finley.gmair.scene.service.impl;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import finley.gmair.scene.client.MachineClient;
import finley.gmair.scene.dao.SceneCommandDAO;
import finley.gmair.scene.dto.Device;
import finley.gmair.scene.dto.SceneDeviceControlOptionDTO;
import finley.gmair.scene.entity.SceneCommandDO;
import finley.gmair.scene.entity.SceneOperationCommand;
import finley.gmair.scene.service.SceneControlService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author : Lyy
 * @create : 2020-12-26 16:11
 **/
@Service
public class SceneControlServiceImpl implements SceneControlService {

    @Resource
    private MachineClient machineClient;

    @Resource
    private SceneCommandDAO sceneCommandDAO;

    /**
     * 根据用户 ID 获取可操作设备及其控制选项
     *
     * @param consumerId 用户ID
     * @return List<SceneDeviceControlOptionDTO>
     */
    @Override
    public List<SceneDeviceControlOptionDTO> getSceneDeviceControlOptions(String consumerId) {
        /*
          1. 根据用户ID获取用户的设备
          2. 根据设备获取设备可控制选项
          3. 将控制选项填充到SceneDeviceControlOptionDTO中
         */
//        ResultData data = machineClient.obtainMachineList(consumerId);
//
//        List<Device> devices = JSON.parseArray(JSON.toJSONString(data.getData()), Device.class);

        // ---------------插桩------------------//
        String device1JSON = "{\"modelName\":\" GM-WY100\", \"bindName\": \"风扇\", \"ownership\": \"OWNER\", \"modelId\": \"MOD20191210iu8h6y78\", \"goodsId\": \"GUO20191208249y7f32\", \"modelCode\": \"10C\", \"modelThumbnail\": \"https://console.gmair.net/image/reception/wind.png\", \"goodsName\": \"果麦冷暖风扇\", \"bindId\": \"CQB2020121796ghxx9\", \"codeValue\": \"10C111A586542\", \"modelBg\": \"https://console.gmair.net/image/reception/wind_bg.png\"}";
        String device2JSON = "{\"modelName\": \"GM420\", \"bindName\": \"ISE2\", \"ownership\": \"SHARE\", \"modelId\": \"MOD20180717nuya5y40\", \"goodsId\": \"GUO20180607ggxi8a96\", \"modelCode\": \"42A\", \"modelThumbnail\": \"https://console.gmair.net/image/reception/420.png\", \"goodsName\": \"果麦新风机\", \"bindId\": \"CQB20201104lxz9zh47\", \"codeValue\": \"42A112A629267\", \"modelBg\": null}";
        Device device1 = JSON.parseObject(device1JSON, Device.class);
        Device device2 = JSON.parseObject(device2JSON, Device.class);
        List<Device> devices = Lists.newArrayList();
        devices.add(device1);
        devices.add(device2);
        // ---------------插桩------------------//


        List<SceneDeviceControlOptionDTO> result = Lists.newArrayList();
        for (Device device : devices) {
            SceneDeviceControlOptionDTO sceneDeviceControlOption = new SceneDeviceControlOptionDTO();
            sceneDeviceControlOption.setConsumerId(consumerId);
            sceneDeviceControlOption.setDevice(device);

            List<SceneCommandDO> sceneCommands = getSceneCommandByGoodsID(device.getGoodsId());

            List<SceneOperationCommand> commands = sceneCommands.stream().map(sceneCommandDO -> {
                SceneOperationCommand operationCommand = new SceneOperationCommand();
                operationCommand.setCommandId(sceneCommandDO.getCommandId());
                operationCommand.setQrCode(device.getCodeValue());
                operationCommand.setCommandOperation(sceneCommandDO.getCommandOperation());
                operationCommand.setCommandComponent(sceneCommandDO.getCommandComponent());
                operationCommand.setCommandName(sceneCommandDO.getCommandName());
                return operationCommand;

            }).collect(Collectors.toList());

            sceneDeviceControlOption.setCommands(commands);
            result.add(sceneDeviceControlOption);
        }

        return result;
    }

    private List<SceneCommandDO> getSceneCommandByGoodsID(String goodsId) {
        return sceneCommandDAO.getSceneCommandByGoodsID(goodsId);
    }
}
