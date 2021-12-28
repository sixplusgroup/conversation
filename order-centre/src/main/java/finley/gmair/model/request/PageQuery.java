package finley.gmair.model.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author ：tsl
 * @date ：Created in 2021-12-25 23:40
 * @description ：
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PageQuery {
    private int pageNo;

    private int pageSize;
}
