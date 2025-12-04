package beans;

import Entity.User;
import EJB.AdminSessionBeanLocal;
import jakarta.ejb.EJB;
import jakarta.enterprise.context.SessionScoped;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import java.io.Serializable;
import jwtrest.TokenProvider;

@Named(value = "loginBean")
@SessionScoped
public class LoginBean implements Serializable {

    @EJB
    private AdminSessionBeanLocal adminSessionBean;

    @Inject
    private TokenProvider tokenProvider;

    private String username;
    private String password;
    private String errorStatus;
    private String message;
    private String token;
    private String role;

    private Integer userid;

    // ⭐ NEW FIELD
    private User loggedInUser;   

    public String login() {
        try {
            User user = adminSessionBean.findUserByUsername(username);

            if (user != null) {
                String hashedInput = adminSessionBean.hashPassword(password);

                if (user.getPassword().equals(hashedInput)) {

                    this.userid = user.getId();
                    this.loggedInUser = user;   // ⭐ STORE FULL USER OBJECT

                    role = (user.getGroupid() != null)
                            ? user.getGroupid().getGroupname()
                            : "User";

                    token = tokenProvider.createTokenWithClaims(
                            user.getUsername(),
                            "",
                            role,
                            false
                    );

                    message = "Login successful! Welcome, " + user.getFullname();
                    errorStatus = "";

                    if (role.equalsIgnoreCase("Admin")) {
                        return "admin.xhtml?faces-redirect=true";
                    } else {
                        return "user.xhtml?faces-redirect=true";
                    }

                } else {
                    errorStatus = "Invalid username or password!";
                    return null;
                }

            } else {
                errorStatus = "Invalid username or password!";
                return null;
            }

        } catch (Exception e) {
            errorStatus = "Error during login: " + e.getMessage();
            e.printStackTrace();
            return null;
        }
    }

    public String logout() {

        username = null;
        password = null;
        token = null;
        role = null;
        message = null;
        errorStatus = null;
        userid = null;
        loggedInUser = null;  // ⭐ CLEAR SESSION USER

        FacesContext.getCurrentInstance().getExternalContext().invalidateSession();

        return "login.xhtml?faces-redirect=true";
    }

    // ⭐ NEW METHOD: Check if user is logged in
    public boolean isLoggedIn() {
        return loggedInUser != null;
    }

    // ⭐ GETTER for full user object
    public User getLoggedInUser() {
        return loggedInUser;
    }

    public void setLoggedInUser(User loggedInUser) {
        this.loggedInUser = loggedInUser;
    }

    // -----------------------------  
    // GETTERS - SETTERS  
    // -----------------------------
    public Integer getUserid() { return userid; }
    public void setUserid(Integer userid) { this.userid = userid; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getErrorStatus() { return errorStatus; }
    public void setErrorStatus(String errorStatus) { this.errorStatus = errorStatus; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public String getToken() { return token; }
    public void setToken(String token) { this.token = token; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }
}
