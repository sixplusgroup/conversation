package finley.gmair.vo.knowledgebase;

import finley.gmair.model.knowledgebase.Knowledge;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class KnowledgePagerVO {
    private Long totalNum;
    private List<KnowledgeVO> knowledgeVOS;
}
