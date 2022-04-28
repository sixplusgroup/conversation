package finley.gmair.model.chatlog.usr.jd;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class JingdongUser {
    String name;
    Integer id;
}
