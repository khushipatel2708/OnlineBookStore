package CDI;

import EJB.UserSessionBeanLocal;
import Entities.User;
import jakarta.ejb.EJB;
import jakarta.enterprise.context.SessionScoped;
import jakarta.inject.Named;
import java.io.Serializable;
import jakarta.faces.context.FacesContext;
import java.io.IOException;
import jakarta.servlet.http.HttpSession;

@Named
@SessionScoped
public class LoginBean implements Serializable {

    private String username;
    private String password;
    private User user;

    @EJB
    private UserSessionBeanLocal userSessionBean;

    public String login() {
        user = userSessionBean.login(username, password);
        if (user != null && user.getGroupMaster() != null && user.getGroupMaster().getGroupName() != null) {
            // ✅ Create session and store user data
            FacesContext facesContext = FacesContext.getCurrentInstance();
            HttpSession session = (HttpSession) facesContext.getExternalContext().getSession(true);
            session.setAttribute("loggedUser", user);

            String role = user.getGroupMaster().getGroupName();
            if (role.equalsIgnoreCase("Admin")) {
                return "adminHome.xhtml?faces-redirect=true";
            } else if (role.equalsIgnoreCase("User")) {
                return "userHome.xhtml?faces-redirect=true";
            }
        }

        return "login.xhtml?faces-redirect=true&error=invalid";
    }

    public void checkSession() throws IOException {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        HttpSession session = (HttpSession) facesContext.getExternalContext().getSession(false);

        // ✅ If session doesn’t exist → redirect to login
        if (session == null || session.getAttribute("loggedUser") == null) {
            facesContext.getExternalContext().redirect("login.xhtml");
        }
    }

    public String logout() throws IOException {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        HttpSession session = (HttpSession) facesContext.getExternalContext().getSession(false);
        if (session != null) {
            session.invalidate();
        }
        return "login.xhtml?faces-redirect=true";
    }

    // Getters & Setters
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }
}