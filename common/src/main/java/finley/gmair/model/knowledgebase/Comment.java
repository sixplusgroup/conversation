package finley.gmair.model.knowledgebase;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Comment {
    private Integer id;
    private Integer knowledgeId;
    private String content;
    private short status;
    private Date createTime;
    private Date solveTime;
    private Integer responserId;
    private short type;
}
