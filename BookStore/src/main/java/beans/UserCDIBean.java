package beans;

import Entity.City;
import Entity.GroupMaster;
import Entity.User;
import client.MyAdminClient;
import jakarta.inject.Named;
import jakarta.faces.view.ViewScoped;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

@Named("userBean")
@ViewScoped
public class UserCDIBean implements Serializable {

    private MyAdminClient client = new MyAdminClient();

    private Integer id;
    private String fullname;
    private String phone;
    private String username;
    private String email;
    private String password;
    private String status;
    private Integer groupId;

    private User selectedUser;

    // ===================== GET LISTS ======================

    public Collection<User> getAllUsers() {
        return client.getAllUsers(Collection.class);
    }

    public Collection<GroupMaster> getAllGroups() {
        return client.getAllGroups(Collection.class);
    }

    // ===================== EDIT USER ======================

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

    // ===================== SAVE USER (ADD/UPDATE) ======================

    public String saveUser() {

        // Build proper object for JSON serialization
        User user = new User();
        user.setFullname(fullname);
        user.setPhone(phone);
        user.setUsername(username);
        user.setEmail(email);

        // If updating AND password is empty → keep old password
        if (password == null || password.trim().isEmpty()) {
            if (selectedUser != null) {
                user.setPassword(selectedUser.getPassword());
            }
        } else {
            user.setPassword(password);
        }

        // default status if empty
        if (status == null || status.trim().isEmpty()) {
            status = "Active";
        }
        user.setStatus(status);

        // Group object is mandatory for REST API
        GroupMaster gm = new GroupMaster();
        gm.setGroupid(groupId);
        user.setGroupid(gm);

        // Decide add or update
        if (selectedUser == null) {
            client.addUser(user);
        } else {
            client.updateUser(user, id.toString());
        }

        clearForm();
        return "login.xhtml?faces-redirect=true";
    }

    // ===================== DELETE ======================

    public void deleteUser(Integer id) {
        client.deleteUser(id.toString());
    }

    // ===================== CLEAR FORM ======================

    public void clearForm() {
        selectedUser = null;
        id = null;
        fullname = "";
        phone = "";
        username = "";
        email = "";
        password = "";
        status = "Active";
        groupId = null;
    }

    // ============== GETTERS / SETTERS ===================

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
    
     private int pageSize = 5;      // Number of rows per page
    private int pageNumber = 1;    // Current page

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getPageNumber() {
        return pageNumber;
    }

    public void setPageNumber(int pageNumber) {
        this.pageNumber = pageNumber;
    }

    public int getTotalPages() {
        int totalGroups = getAllUsers().size();
        return (int) Math.ceil((double) totalGroups / pageSize);
    }

    public Collection<User> getPagedGroups() {
        List<User> all = new ArrayList<>(getAllUsers());
        int fromIndex = (pageNumber - 1) * pageSize;
        int toIndex = Math.min(fromIndex + pageSize, all.size());
        if (fromIndex > all.size()) {
            return Collections.emptyList();
        }
        return all.subList(fromIndex, toIndex);
    }

    public void nextPage() {
        if (pageNumber < getTotalPages()) {
            pageNumber++;
        }
    }

    public void previousPage() {
        if (pageNumber > 1) {
            pageNumber--;
        }
    }
}
