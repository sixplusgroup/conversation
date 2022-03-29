package finley.gmair.utils;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TagsPageParam {
    private Integer pageSize;

    private Integer pageNum;

    //tagController.getByTags用到
    private List<Integer> tag_ids;
}
