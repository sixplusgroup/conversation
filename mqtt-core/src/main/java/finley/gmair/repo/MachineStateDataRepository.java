package finley.gmair.repo;

import finley.gmair.model.machine.MachineStateData;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface MachineStateDataRepository extends MongoRepository<MachineStateData, String> {

}
