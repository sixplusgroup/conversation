package finley.gmair.repo;

import finley.gmair.model.machine.MachinePartialStatus;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface MachinePartialStatusRepository extends MongoRepository<MachinePartialStatus, String> {

}
