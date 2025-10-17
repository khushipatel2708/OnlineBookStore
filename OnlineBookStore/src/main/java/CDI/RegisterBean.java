package CDI;

import EJB.UserSessionBeanLocal;
import Entity.GroupMaster;
import Entity.User;
import jakarta.ejb.EJB;
import jakarta.inject.Named;
import jakarta.enterprise.context.RequestScoped;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;

@Named(value = "registerBean")
@RequestScoped
public class RegisterBean {

    @EJB
    private UserSessionBeanLocal userSessionBean;

    private String fullname;
    private String phone;
    private String username;
    private String email;
    private String password;
    private String role; // "admin" or "user"

    public String register() {
        try {
            User user = new User();
            user.setFullname(fullname);
            user.setPhone(phone);
            user.setUsername(username);
            user.setEmail(email);
            user.setPassword(password);
            user.setStatus("Active");

            GroupMaster gm = new GroupMaster();
            if ("admin".equalsIgnoreCase(role))
                gm.setGroupid(1); // assuming admin is ID 1
            else
                gm.setGroupid(2); // assuming user is ID 2

            user.setGroupid(gm);

            if (userSessionBean.register(user)) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Registration successful! Please login."));
                return "login.xhtml?faces-redirect=true";
            } else {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Error in registration."));
                return null;
            }
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Registration failed: " + e.getMessage()));
            return null;
        }
    }

    // Getters and Setters
    public String getFullname() { return fullname; }
    public void setFullname(String fullname) { this.fullname = fullname; }
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }
}