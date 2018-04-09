package finley.gmair.model.admin;

import finley.gmair.model.Entity;

public class Admin extends Entity {
    private String adminId;

    private String email;

    private String username;

    private String password;

    public Admin() {
        super();
        blockFlag = true;
    }

    public Admin(String email, String username, String password) {
        this();
        this.email = email;
        this.username = username;
        this.password = password;
    }

    public String getAdminId() {
        return adminId;
    }

    public void setAdminId(String adminId) {
        this.adminId = adminId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
