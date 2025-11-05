package CDI;

import EJB.UserSessionBeanLocal;
import Entity.User;
import jakarta.ejb.EJB;
import jakarta.enterprise.context.RequestScoped;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Named;
import java.io.Serializable;

@Named(value = "changePasswordBean")
@RequestScoped
public class ChangePasswordBean implements Serializable {

    @EJB
    private UserSessionBeanLocal userBean;

    private String oldPassword;
    private String newPassword;
    private String confirmPassword;

    // --- METHOD TO CHANGE PASSWORD ---
    public String changePassword() {
        FacesContext context = FacesContext.getCurrentInstance();

        try {
            User loggedInUser = (User) context.getExternalContext().getSessionMap().get("user");

            if (loggedInUser == null) {
                context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "User not logged in", null));
                return "login.xhtml?faces-redirect=true";
            }

            if (!newPassword.equals(confirmPassword)) {
                context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Passwords do not match", null));
                return null;
            }

            boolean success = userBean.changePassword(loggedInUser.getUsername(), oldPassword, newPassword);

            if (success) {
                context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Password changed successfully", null));
                return "profile.xhtml?faces-redirect=true";
            } else {
                context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Old password is incorrect", null));
                return null;
            }

        } catch (Exception e) {
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error: " + e.getMessage(), null));
            return null;
        }
    }

    // --- GETTERS & SETTERS ---
    public String getOldPassword() { return oldPassword; }
    public void setOldPassword(String oldPassword) { this.oldPassword = oldPassword; }

    public String getNewPassword() { return newPassword; }
    public void setNewPassword(String newPassword) { this.newPassword = newPassword; }

    public String getConfirmPassword() { return confirmPassword; }
    public void setConfirmPassword(String confirmPassword) { this.confirmPassword = confirmPassword; }
}
