package finley.gmair.repo;

import finley.gmair.model.machine.MachineSensorData;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface MachineSensorDataRepository extends MongoRepository<MachineSensorData, String> {

}
