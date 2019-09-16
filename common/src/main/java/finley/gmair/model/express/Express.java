package finley.gmair.model.express;

public class Express {

    //对应 status，订阅状态
    private String status;

    //对应state 快递当前状态
    private String state;

    //对应com 快递公司
    private String company;

    //对应nu 快递单号
    private String expressNum;

    //对应data 快递详细信息
    private String data;

    public Express(String status, String state, String company, String expressNum, String data) {
        this.status = status;
        this.state = state;
        this.company = company;
        this.expressNum = expressNum;
        this.data = data;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getExpressNum() {
        return expressNum;
    }

    public void setExpressNum(String expressNum) {
        this.expressNum = expressNum;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

}
