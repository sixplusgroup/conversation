package finley.gmair.repo;

import finley.gmair.model.machine.MachineStatusV3;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * @ClassName: MachineStatusV3Repository
 * @Description: TODO
 * @Author fan
 * @Date 2019/5/20 11:08 AM
 */
public interface MachineStatusV3Repository extends MongoRepository<MachineStatusV3, String> {
}
