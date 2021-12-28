package finley.gmair.model.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @author ：tsl
 * @date ：Created in 2021-12-27 19:58
 * @description ：
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PullRequestDTO {
    /**
     * 开始时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date startTime;

    /**
     * 结束时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date endTime;

    /**
     * 根据修改时间排序true, 根据创建时间排序false
     */
    private Boolean isSortByModified;
}
