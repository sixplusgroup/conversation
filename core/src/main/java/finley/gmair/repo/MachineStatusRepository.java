package finley.gmair.repo;

import finley.gmair.model.machine.MachineStatusV2;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface CommunicationRepository extends MongoRepository<MachineStatusV2, String> {
    
}
