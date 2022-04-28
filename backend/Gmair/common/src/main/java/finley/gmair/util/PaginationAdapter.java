package finley.gmair.util;

import cn.hutool.core.util.PageUtil;
import lombok.Data;

/**
 * @Author Joby
 * @Date 10/19/2021 9:25 PM
 * @Description
 */
@Data
public class PaginationAdapter {
    private int begin;

    private int size;

    public PaginationAdapter(PaginationParam paginationParam) {
        int[] startEnd = PageUtil.transToStartEnd((int) paginationParam.getCurrent(), (int) paginationParam.getSize());
        this.begin = startEnd[0];
        this.size = (int)paginationParam.getSize();
    }
}
