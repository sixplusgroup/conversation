package finley.gmair.vo.chatlogReview;

import cn.hutool.core.date.DateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SentiGraphVO {
    private boolean isWaiterSend;
    private String time;
    private double emoRate;
    private String content;
}
