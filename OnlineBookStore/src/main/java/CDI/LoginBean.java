package CDI;

import EJB.UserSessionBeanLocal;
import Entity.User;
import jakarta.ejb.EJB;
import jakarta.inject.Named;
import jakarta.enterprise.context.SessionScoped;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import java.io.Serializable;

@Named(value = "loginBean")
@SessionScoped
public class LoginBean implements Serializable {

    @EJB
    private UserSessionBeanLocal userSessionBean;

    private String username;
    private String password;
    private User loggedInUser;

    // ---------------- LOGIN METHOD -----------------
    public String login() {
        try {
            loggedInUser = userSessionBean.login(username, password);

            if (loggedInUser != null) {
                // Save user in session
                FacesContext.getCurrentInstance().getExternalContext()
                        .getSessionMap().put("user", loggedInUser);

                // Get role name (make sure getGroupid().getGroupname() works)
                String role = loggedInUser.getGroupid().getGroupname();

                // Redirect based on role
                if ("admin".equalsIgnoreCase(role)) {
                    return "/adminPage.xhtml?faces-redirect=true";
                } else if ("user".equalsIgnoreCase(role)) {
                    return "/userPage.xhtml?faces-redirect=true";
                } else {
                    FacesContext.getCurrentInstance().addMessage(null,
                        new FacesMessage("Unknown role: " + role));
                    return null;
                }

            } else {
                FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR,
                        "Invalid username or password", null));
                return null;
            }

        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_ERROR,
                    "Login error: " + e.getMessage(), null));
            e.printStackTrace();
            return null;
        }
    }

    // ---------------- LOGOUT METHOD -----------------
    public String logout() {
        FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
        return "/login.xhtml?faces-redirect=true";
    }

    // ---------------- GETTERS & SETTERS -----------------
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

    public User getLoggedInUser() {
        return loggedInUser;
    }

    public void setLoggedInUser(User loggedInUser) {
        this.loggedInUser = loggedInUser;
    }
}
