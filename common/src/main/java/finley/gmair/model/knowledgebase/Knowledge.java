package finley.gmair.model.knowledgebase;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Knowledge {
    private Integer knowledge_id;
    private Integer status;
    private Integer knowledge_type;
    private String title;
    private String content;
    private Integer views;
}
