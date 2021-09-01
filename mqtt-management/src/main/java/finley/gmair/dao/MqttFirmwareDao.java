package finley.gmair.dao;

import finley.gmair.model.mqttManagement.Firmware;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface MqttFirmwareDao {
    int insert(Firmware firmware);

    List<Firmware> query(Map<String, Object> condition);

    int update(Map<String, Object> condition);
}
