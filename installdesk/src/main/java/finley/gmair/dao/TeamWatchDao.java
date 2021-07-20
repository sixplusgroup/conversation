package finley.gmair.dao;

import finley.gmair.model.installation.TeamWatch;
import finley.gmair.util.ResultData;

import java.util.Map;

/**
 * @ClassName: TeamWatchDao
 * @Description: TODO
 * @Author fan
 * @Date 2019/4/3 3:45 PM
 */
public interface TeamWatchDao {
    ResultData insert(TeamWatch tw);

    ResultData query(Map<String, Object> condition);

    ResultData block(Map<String, Object> condition);

    ResultData remove(String watchId);

    ResultData queryMemberTeam(Map<String, Object> condition);
}
