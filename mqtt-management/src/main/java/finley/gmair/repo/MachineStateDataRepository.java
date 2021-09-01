package finley.gmair.repo;

import finley.gmair.model.machine.MachineStateData;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * @author fan
 * @date 2019/5/20 11:08 AM
 */
public interface MachineStateDataRepository extends MongoRepository<MachineStateData, String> {

}
