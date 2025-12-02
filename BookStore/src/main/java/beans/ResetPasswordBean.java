package beans;

import client.MyAdminClient;
import Entity.User;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Named;
import jakarta.ws.rs.core.Response;

@Named(value = "resetPasswordBean")
@RequestScoped
public class ResetPasswordBean {

    private String email;
    private String newPassword;

    // GETTERS & SETTERS
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getNewPassword() { return newPassword; }
    public void setNewPassword(String newPassword) { this.newPassword = newPassword; }

    public String reset() {

        try {
            MyAdminClient client = new MyAdminClient();
            User u = new User();
            u.setEmail(email);
            u.setPassword(newPassword);

            Response res = client.resetPassword(u);

            if (res.getStatus() == 200) {
                return "login.xhtml?faces-redirect=true";
            } else {
                return null;
            }

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
