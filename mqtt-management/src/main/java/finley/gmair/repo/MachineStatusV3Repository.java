package finley.gmair.repo;

import finley.gmair.model.machine.MachineStatusV3;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * @author fan
 * @date 2019/5/20 11:08 AM
 */
public interface MachineStatusV3Repository extends MongoRepository<MachineStatusV3, String> {
}
