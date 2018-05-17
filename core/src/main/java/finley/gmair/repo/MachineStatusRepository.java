package finley.gmair.repo;

import finley.gmair.model.machine.MachineStatus;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface MachineStatusRepository extends MongoRepository<MachineStatus, String> {

}
