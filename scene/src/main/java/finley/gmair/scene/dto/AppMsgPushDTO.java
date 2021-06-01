package finley.gmair.scene.dto;

import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * @author : Lyy
 * @create : 2021-01-15 14:49
 **/
@Data
public class AppMsgPushDTO {
    private String title;
    private String content;
    private Map<String, String> extrasMap;
    private List<String> alias;
}
