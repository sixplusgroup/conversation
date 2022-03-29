package finley.gmair.vo.knowledgebase;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class KnowledgeVO {
    private Integer id;
    private String title;
    private String content;
    private Integer status;
    private Integer views;
}
