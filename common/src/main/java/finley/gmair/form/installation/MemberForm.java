package finley.gmair.form.installation;

public class MemberForm {
    private String teamId;
    private String memberPhone;
    private String memberName;
    private int memberRole;

    public String getTeamId() {
        return teamId;
    }

    public void setTeamId(String teamId) {
        this.teamId = teamId;
    }

    public String getMemberPhone() {
        return memberPhone;
    }

    public void setMemberPhone(String memberPhone) {
        this.memberPhone = memberPhone;
    }

    public String getMemberName() {
        return memberName;
    }

    public void setMemberName(String memberName) {
        this.memberName = memberName;
    }

    public int getMemberRole() {
        return memberRole;
    }

    public void setMemberRole(int memberRole) {
        this.memberRole = memberRole;
    }
}
