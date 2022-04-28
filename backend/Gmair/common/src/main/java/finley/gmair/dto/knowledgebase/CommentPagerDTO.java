package finley.gmair.dto.knowledgebase;

import finley.gmair.vo.knowledgebase.CommentVO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CommentPagerDTO {
    private Long totalNum;
    private List<CommentDTO> commentDTOS;
}
