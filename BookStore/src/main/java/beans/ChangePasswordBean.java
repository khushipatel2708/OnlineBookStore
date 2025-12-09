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
    FacesContext.getCurrentInstance().addMessage(null,
        new FacesMessage(FacesMessage.SEVERITY_INFO, 
        "Password changed successfully!", null));
    return null; // stay on same page
} else {
    FacesContext.getCurrentInstance().addMessage(null,
        new FacesMessage(FacesMessage.SEVERITY_ERROR, 
        "Failed to change password!", null));
    return null;
}


        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
