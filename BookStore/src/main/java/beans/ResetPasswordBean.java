package beans;

import client.MyAdminClient;
import Entity.User;
import jakarta.enterprise.context.RequestScoped;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
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

        FacesContext facesContext = FacesContext.getCurrentInstance();

        if (res.getStatus() == 200) {

            // ✅ keep message after redirect
            facesContext.getExternalContext()
                        .getFlash().setKeepMessages(true);

            facesContext.addMessage(null,
                new FacesMessage(
                    FacesMessage.SEVERITY_INFO,
                    "Password reset successfully!",
                    null
                )
            );

            // ✅ redirect to profile page
            return null;

        } else {

            facesContext.addMessage(null,
                new FacesMessage(
                    FacesMessage.SEVERITY_ERROR,
                    "Failed to reset password. Please try again.",
                    null
                )
            );
            return null; // stay on same page
        }

    } catch (Exception e) {
        e.printStackTrace();

        FacesContext.getCurrentInstance().addMessage(null,
            new FacesMessage(
                FacesMessage.SEVERITY_ERROR,
                "Something went wrong. Please try again later.",
                null
            )
        );

        return null;
    }
}

}
