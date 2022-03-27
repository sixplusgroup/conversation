package finley.gmair.dto.knowledgebase;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CommentDTO {
    private Integer id;
    private Integer knowledgeId;
    private String content;
    private short status;
    private String createTime;
    private String solveTime;
    private Integer responserId;
    private short type;
}
