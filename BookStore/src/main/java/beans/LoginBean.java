package beans;

import Entity.User;
import EJB.AdminSessionBeanLocal;
import jakarta.ejb.EJB;
import jakarta.enterprise.context.SessionScoped;
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

    public String login() {
        try {
            User user = adminSessionBean.findUserByUsername(username);

            if (user != null && user.getPassword().equals(password)) {
                role = (user.getGroupid() != null)
                        ? user.getGroupid().getGroupname()
                        : "USER";

                // ✅ Generate token
                token = tokenProvider.createTokenWithClaims(
                        user.getUsername(),
                        user.getPassword(),
                        role,
                        false
                );

                System.out.println("Generated Token: " + token);

                message = "Login successful! Welcome, " + user.getFullname();
                errorStatus = "";

                // ✅ Redirect based on role
                if (role.equalsIgnoreCase("ADMIN")) {
                    return "admin.xhtml";
                } else if (role.equalsIgnoreCase("USER")) {
                    return "user.xhtml";
                } else {
                    return "login.xhtml";
                }

            } else {
                errorStatus = "Invalid username or password!";
                message = "";
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
        message = "You have been logged out successfully.";
        return "login.xhtml?faces-redirect=true";
    }

    // Getters & Setters
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
