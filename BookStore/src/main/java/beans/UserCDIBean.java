package beans;

import EJB.AdminSessionBeanLocal;
import Entity.GroupMaster;
import Entity.User;
import jakarta.inject.Named;
import jakarta.faces.view.ViewScoped;
import jakarta.ejb.EJB;
import java.io.Serializable;
import java.util.Collection;

@Named("userBean")
@ViewScoped
public class UserCDIBean implements Serializable {

    @EJB
    AdminSessionBeanLocal admin;

    private Integer id;
    private String fullname;
    private String phone;
    private String username;
    private String email;
    private String password;
    private String status;
    private Integer groupId;

    private User selectedUser;

    public Collection<User> getAllUsers() {
        return admin.getAllUsers();
    }

    public Collection<GroupMaster> getAllGroups() {
        return admin.getAllGroups();
    }

    public void editUser(User u) {
        selectedUser = u;
        id = u.getId();
        fullname = u.getFullname();
        phone = u.getPhone();
        username = u.getUsername();
        email = u.getEmail();
        password = u.getPassword();
        status = u.getStatus();
        groupId = u.getGroupid().getGroupid();
    }

    public String saveUser() {
        if (selectedUser == null) { // ADD
            admin.addUser(fullname, phone, username, email, password, status, groupId);
        } else {                    // UPDATE
            admin.updateUser(id, fullname, phone, username, email, password, status, groupId);
        }
        clearForm();
        return "user.xhtml?faces-redirect=true";
    }

    public void deleteUser(Integer id) {
        admin.deleteUser(id);
    }

    public void clearForm() {
        selectedUser = null;
        fullname = "";
        phone = "";
        username = "";
        email = "";
        password = "";
        status = "";
        groupId = null;
    }

    // ---------- Getters & Setters ----------

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

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

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public Integer getGroupId() { return groupId; }
    public void setGroupId(Integer groupId) { this.groupId = groupId; }

    public User getSelectedUser() { return selectedUser; }
    public void setSelectedUser(User selectedUser) { this.selectedUser = selectedUser; }
}
