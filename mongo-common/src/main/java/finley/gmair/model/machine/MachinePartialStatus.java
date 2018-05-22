package finley.gmair.model.machine;

import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "machine_partial_status")
public class MachinePartialStatus extends Entity {
    private String uid;

    private String name;

    private Object data;

    public MachinePartialStatus() {
        super();
    }

    public MachinePartialStatus(String uid, String name, Object data) {
        this();
        this.uid = uid;
        this.name = name;
        this.data = data;
    }

    public MachinePartialStatus(String uid, String name, Object data, long timestamp) {
        this(uid, name, data);
        this.setCreateAt(timestamp);
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
