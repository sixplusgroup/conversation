package finley.gmair.model.installation;

import finley.gmair.model.Entity;

public class Team extends Entity {
    private String teamId;
    private String teamName;
    private String teamArea;
    private String teamDescription;
    public Team()
    {
        super();
    }

    public Team(String teamName, String teamArea, String teamDescription) {
        this();
        this.teamName = teamName;
        this.teamArea = teamArea;
        this.teamDescription = teamDescription;
    }

    public String getTeamId() {
        return teamId;
    }

    public void setTeamId(String teamId) {
        this.teamId = teamId;
    }

    public String getTeamName() {
        return teamName;
    }

    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }

    public String getTeamArea() {
        return teamArea;
    }

    public void setTeamArea(String teamArea) {
        this.teamArea = teamArea;
    }

    public String getTeamDescription() {
        return teamDescription;
    }

    public void setTeamDescription(String teamDescription) {
        this.teamDescription = teamDescription;
    }
}
