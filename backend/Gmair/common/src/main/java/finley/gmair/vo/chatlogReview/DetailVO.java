package finley.gmair.vo.chatlogReview;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DetailVO {
    private String pid;
    private String cname;
    private int session_id;
    private List<SentiGraphVO> sentigraph;
    private double customerAverageScore;
    private int customerExtremeNegativeCount;
    private int waiterNegativeCount;
}
