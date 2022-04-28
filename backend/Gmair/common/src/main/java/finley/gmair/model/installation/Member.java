package finley.gmair.model.installation;

import finley.gmair.model.Entity;

public class Member extends Entity {
    private String memberId;
    private String teamId;
    private String memberPhone;
    private String memberName;
    private String wechatId;
    private MemberRole memberRole;

    public Member() {
        super();
    }

    public Member(String teamId, String memberPhone, String memberName, MemberRole memberRole) {
        this();
        this.teamId = teamId;
        this.memberPhone = memberPhone;
        this.memberName = memberName;
        this.memberRole = memberRole;
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

    public String getWechatId() {
        return wechatId;
    }

    public void setWechatId(String wechatId) {
        this.wechatId = wechatId;
    }

    public MemberRole getMemberRole() {
        return memberRole;
    }

    public void setMemberRole(MemberRole memberRole) {
        this.memberRole = memberRole;
    }
}
