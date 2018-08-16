package finley.gmair.repo;

import finley.gmair.model.machine.MachineV1Status;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface MachineV1StatusRepository extends MongoRepository<MachineV1Status, String> {
}
