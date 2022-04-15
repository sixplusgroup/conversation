package finley.gmair.model.knowledgebaseAuth;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class KnowledgebasePermission {
    private int id;
    private String authorize;
    private String describe;
}
