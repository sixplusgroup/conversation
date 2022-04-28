package finley.gmair.model.knowledgebase;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TagRelation {
    private Integer id;
    private Integer tag_id;
    private Integer knowledge_id;
}
