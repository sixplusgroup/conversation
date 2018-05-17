package finley.gmair.model.machine;

import finley.gmair.model.Entity;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "machine_server_url")
public class MachineServerURLStatus extends Entity {
    private String uid;

    private String server;

    public MachineServerURLStatus() {
        super();
    }

    public MachineServerURLStatus(String uid, String server) {
        this();
        this.uid = uid;
        this.server = server;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getServer() {
        return server;
    }

    public void setServer(String server) {
        this.server = server;
    }
}
