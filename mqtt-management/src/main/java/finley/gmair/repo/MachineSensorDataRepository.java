package finley.gmair.repo;

import finley.gmair.model.machine.MachineSensorData;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * @author fan
 * @date 2019/5/20 11:08 AM
 */
public interface MachineSensorDataRepository extends MongoRepository<MachineSensorData, String> {

}
