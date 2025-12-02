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

    private Integer userid;   // 🔹 ADDED THIS FIELD

    public String login() {
        try {
            User user = adminSessionBean.findUserByUsername(username);

            if (user != null) {

                String hashedInput = adminSessionBean.hashPassword(password);

                if (user.getPassword().equals(hashedInput)) {

                    // 🔹 Store USER ID for later use
                    this.userid = user.getId();

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

        FacesContext.getCurrentInstance().getExternalContext().invalidateSession();

        return "login.xhtml?faces-redirect=true";
    }

    // -----------------------------
    // GETTERS - SETTERS
    // -----------------------------

    public Integer getUserid() {   // 🔹 IMPORTANT GETTER
        return userid;
    }

    public void setUserid(Integer userid) {
        this.userid = userid;
    }

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
