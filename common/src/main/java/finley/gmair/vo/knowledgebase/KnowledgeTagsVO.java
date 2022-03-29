package finley.gmair.vo.knowledgebase;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class KnowledgeTagsVO {
    private Integer knowledge_id;
    private List<Integer> tag_ids;
}
