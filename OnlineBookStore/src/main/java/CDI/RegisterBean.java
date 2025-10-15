package CDI;

import EJB.UserSessionBeanLocal;
import Entities.GroupMaster;
import Entities.User;
import jakarta.ejb.EJB;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Named;

@Named
@RequestScoped
public class RegisterBean {

    private String fullname;
    private String phone;
    private String username;
    private String email;
    private String password;
    private int roleId; // <-- new field for role selection

    @EJB
    private UserSessionBeanLocal userSessionBean;

    public String register() {
        if (roleId == 0) {
            return "register.xhtml?faces-redirect=true"; // no role selected
        }

        User user = new User();
        user.setFullname(fullname);
        user.setPhone(phone);
        user.setUsername(username);
        user.setEmail(email);
        user.setPassword(password);
        user.setStatus("Active");

        GroupMaster gm = new GroupMaster();
        gm.setGroupId(roleId);  // <-- use selected role
        user.setGroupMaster(gm);

        userSessionBean.registerUser(user);
        return "login.xhtml?faces-redirect=true";
    }

    // Getters & Setters
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

    public int getRoleId() { return roleId; }
    public void setRoleId(int roleId) { this.roleId = roleId; }
}
