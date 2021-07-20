package finley.gmair.model.installation;

public class MemberTeam {
    private String memberId;//不加get set方法使这一项没有返回值减少表连接的数据冗余
    private Member member;

    public MemberTeam(){}
    public Member getMember() {
        return member;
    }
    public void setMember(Member member) {
        this.member = member;
    }
}
