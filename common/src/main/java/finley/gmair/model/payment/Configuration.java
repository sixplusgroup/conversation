package finley.gmair.model.payment;

public class Configuration {

    private String environment;
    private String payUrl;

    public Configuration() {
        super();
    }

    public String getEnvironment() {
        return environment;
    }

    public void setEncironment(String environment) {
        this.environment = environment;
    }

    public String getPayUrl() {
        return payUrl;
    }

    public void setPayUrl(String payUrl) {
        this.payUrl = payUrl;
    }
}
