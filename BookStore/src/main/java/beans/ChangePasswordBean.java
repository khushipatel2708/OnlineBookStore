package beans;

import client.MyAdminClient;
import Entity.User;
import jakarta.enterprise.context.RequestScoped;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.ws.rs.core.Response;

@Named(value = "changePasswordBean")
@RequestScoped
public class ChangePasswordBean {

    private String oldPassword;
    private String newPassword;

    @Inject
    private LoginBean loginBean;  // session userId access

    // GETTERS / SETTERS
    public String getOldPassword() { return oldPassword; }
    public void setOldPassword(String oldPassword) { this.oldPassword = oldPassword; }

    public String getNewPassword() { return newPassword; }
    public void setNewPassword(String newPassword) { this.newPassword = newPassword; }

   public String change() {

    try {
        MyAdminClient client = new MyAdminClient();
        User u = new User();
        u.setStatus(oldPassword);     // old password
        u.setPassword(newPassword);   // new password

        String userId = loginBean.getUserid().toString();

        Response res = client.changePassword(u, userId);

        if (res.getStatus() == 200) {

            // SUCCESS MESSAGE (optional – redirect ma message store nathi thato)
            FacesContext.getCurrentInstance().getExternalContext()
                .getFlash().setKeepMessages(true);

            FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_INFO,
                "Password changed successfully!", null));

            // 👉 redirect to UserProfile.jsf
            return "/UserProfile.xhtml?faces-redirect=true";

        } else {

            // FAIL MESSAGE
            FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_ERROR,
                "Password change failed. Please try again.", null));

            return null; // same page
        }

    } catch (Exception e) {
        e.printStackTrace();

        FacesContext.getCurrentInstance().addMessage(null,
            new FacesMessage(FacesMessage.SEVERITY_ERROR,
            "Something went wrong. Please try again.", null));

        return null;
    }
}

}
