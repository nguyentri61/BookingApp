package vn.iostar.signupsignindemo.Model;

public class User {
    private String email;
    private String password;
    private boolean active = false;

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public boolean isActive() { return active; }
    public void setActive(boolean active) { this.active = active; }
}
