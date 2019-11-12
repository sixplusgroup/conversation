package finley.gmair.model.air;

import finley.gmair.model.Entity;

public class MojiToken extends Entity {

    private String tokenId;

    private String token;

    private String password;

    private String url;

    private String base;

    public MojiToken() {
        super();
    }

    public MojiToken(String token, String password, String url, String base) {
        this();
        this.token = token;
        this.password = password;
        this.url = url;
        this.base = base;
    }

    public String getTokenId() {
        return tokenId;
    }

    public void setTokenId(String tokenId) {
        this.tokenId = tokenId;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getBase() {
        return base;
    }

    public void setBase(String base) {
        this.base = base;
    }
}
