package finley.gmair.vo.knowledgebase;

import finley.gmair.enums.knowledgeBase.CommentStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CommentVO {
  private Integer id;
    private Integer knowledgeId;
    private String content;
    //在插入评论时默认给予待解决的初始属性
    private String status = CommentStatus.UNRESOLVED.getValue();
    private String createTime;
    private String solveTime;
    private Integer responserId;
    private String type;
}
