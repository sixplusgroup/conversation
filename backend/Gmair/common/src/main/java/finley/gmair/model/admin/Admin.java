package finley.gmair.model.admin;

import finley.gmair.model.Entity;

public class Admin extends Entity {
    private String adminId;

    private String email;

    private String username;

    private String password;

    private AdminRole role;

    public Admin() {
        super();
        blockFlag = true;
    }

    public Admin(String email, String username, String password, AdminRole role){
        this();
        this.email = email;
        this.username = username;
        this.password = password;
        this.role = role;
    }

    public Admin(String email, String username, String password, int role) {
        this();
        this.email = email;
        this.username = username;
        this.password = password;
        switch (role){
            case 0:
                this.role = AdminRole.MANAGER;
                break;
            case 1:
                this.role = AdminRole.INSTALL;
                break;
            case 2:
                this.role = AdminRole.CHECK;
                break;
            case 3:
                this.role = AdminRole.CUSTOM_SERVICE;
                break;
            case 4:
                this.role = AdminRole.MESSAGE;
                break;
            default:
                this.role = null;
        }
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

    public AdminRole getRole() {
        return role;
    }

    public void setRole(AdminRole role) {
        this.role = role;
    }
}
