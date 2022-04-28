package finley.gmair.model.maintenance;

import finley.gmair.model.log.UserMachineOperationLog;
import lombok.Data;

import java.util.List;

/**
 * 用户操作历史
 *
 * @author lycheeshell
 * @date 2021/1/28 23:54
 */
@Data
public class UserActionHistoryDTO {

    /**
     * 分页的操作记录
     */
    private List<UserMachineOperationLog> history;

    /**
     * 操作的总数，分页查询
     */
    private Integer total;

}
