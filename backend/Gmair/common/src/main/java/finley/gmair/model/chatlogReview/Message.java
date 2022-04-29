package finley.gmair.model.chatlogReview;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Message {
    /**
     *  message_id     int  not null comment '主键id' auto_increment primary key,
     *     session_id     int  not null comment '会话id',
     *     content        text not null comment '聊天内容',
     *     is_from_waiter boolean default false comment '是否由客服发送',
     *     timestamp      long not null comment '发送时间',
     *     label          varchar(10) comment '消息评价，0-negative，1-neutral，2-positive',
     *     score          double comment '消息评分',
     */
    private int messageId;
    private int sessionId;
    private String content;
    private boolean isFromWaiter;
    private Long timestamp;
    private String label;
    private double score;
}
