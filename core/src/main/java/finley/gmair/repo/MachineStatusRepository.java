package finley.gmair.repo;

import finley.gmair.model.machine.v2.MachineLiveStatus;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface MachineStatusRepository extends MongoRepository<MachineLiveStatus, String> {

}
