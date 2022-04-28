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
public class CommentPagerVO {
    private Long totalNum;
    private List<CommentVO> commentVOList;
}
