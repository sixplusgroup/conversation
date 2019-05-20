package finley.gmair.model.installation;

import finley.gmair.model.Entity;

/**
 * @ClassName: TeamWatch
 * @Description: TODO
 * @Author fan
 * @Date 2019/4/3 2:59 PM
 */
public class TeamWatch extends Entity {
    private String watchId;

    private String memberId;

    private String teamId;

    public TeamWatch() {
        super();
    }

    public TeamWatch(String memberId, String teamId) {
        this();
        this.memberId = memberId;
        this.teamId = teamId;
    }

    public String getWatchId() {
        return watchId;
    }

    public void setWatchId(String watchId) {
        this.watchId = watchId;
    }

    public String getMemberId() {
        return memberId;
    }

    public void setMemberId(String memberId) {
        this.memberId = memberId;
    }

    public String getTeamId() {
        return teamId;
    }

    public void setTeamId(String teamId) {
        this.teamId = teamId;
    }
}
