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
        FacesContext context = FacesContext.getCurrentInstance();
        try {
            // --- FETCH GROUP FROM DATABASE ---
            GroupMaster gm;
            if ("admin".equalsIgnoreCase(role)) {
                gm = userSessionBean.getGroupById(1); // fetch admin group
            } else {
                gm = userSessionBean.getGroupById(2); // fetch user group
            }

            // --- CREATE USER ---
            User user = new User();
            user.setFullname(fullname);
            user.setPhone(phone);
            user.setUsername(username);
            user.setEmail(email);
            user.setPassword(password); // optionally hash password
            user.setStatus("Active");
            user.setGroupid(gm);

            if (userSessionBean.register(user)) {
                context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Registration successful! Please login.", null));
                return "login.xhtml?faces-redirect=true";
            } else {
                context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error in registration.", null));
                return null;
            }

        } catch (Exception e) {
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Registration failed: " + e.getMessage(), null));
            e.printStackTrace();
            return null;
        }
    }

    // --- GETTERS & SETTERS ---
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