package CDI;

import EJB.UserSessionBeanLocal;
import jakarta.ejb.EJB;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Named;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;

@Named(value = "resetPasswordBean")
@RequestScoped
public class ResetPasswordBean {

    @EJB
    private UserSessionBeanLocal userSessionBean;

    private String email;
    private String newPassword;

    // Getters and Setters
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public String getNewPassword() {
        return newPassword;
    }
    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    // ✅ Main method to reset password
    public void resetPassword() {
        boolean result = userSessionBean.resetPasswordByEmail(email, newPassword);
        FacesContext context = FacesContext.getCurrentInstance();

        if (result) {
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,
                    "Password Reset Successful!", "You can now log in with your new password."));
        } else {
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
                    "Error!", "No user found with this email."));
        }
    }
}
