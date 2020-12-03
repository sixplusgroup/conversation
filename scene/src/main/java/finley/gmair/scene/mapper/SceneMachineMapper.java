package finley.gmair.scene.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import finley.gmair.scene.entity.SceneMachineDO;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author : Lyy
 * @description : 场景内设备的查询mapper
 * @create : 2020-12-03 19:52
 **/
@Mapper
public interface SceneMachineMapper extends BaseMapper<SceneMachineDO> {

}
