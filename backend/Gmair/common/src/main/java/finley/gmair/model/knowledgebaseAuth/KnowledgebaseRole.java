package finley.gmair.model.knowledgebaseAuth;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class KnowledgebaseRole {
    private int id;
    private String name;
    private String description;
}
