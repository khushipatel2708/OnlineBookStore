package beans;

import Entity.GroupMaster;
import Entity.User;
import client.MyAdminClient;
import jakarta.enterprise.context.SessionScoped;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Inject;
import jakarta.inject.Named;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@Named("adminUserBean")
@SessionScoped
public class AdminUserCDIBean implements Serializable {

    private static final long serialVersionUID = 1L;

    private MyAdminClient adminClient = new MyAdminClient();

    @Inject
    private LoginBean loginBean; // optional if you use login checks like isAdmin()

    private Integer id;
    private String username;
    private String password;
    private String fullname;
    private String email;
    private String phone;
    private Integer groupId;
    private String status = "Active";

    private boolean editMode = false;

    private boolean passwordDisabled = true; // default

    public boolean isPasswordDisabled() {
        return passwordDisabled;
    }

    public void enablePasswordChange() {
        this.passwordDisabled = false;
    }

    // search / list
    private Collection<User> searchResults = null;

    // pagination (optional, similar to BookCDIBean)
    private int pageSize = 10;
    private int pageNumber = 1;

    // ========== UTILITIES ==========
    public boolean isAdmin() {
        return "Admin".equalsIgnoreCase(loginBean != null ? loginBean.getRole() : "Admin");
    }

    public String saveOrUpdate() {
        if (editMode) {
            return updateUser();
        } else {
            return addUser();
        }
    }

    // Converts client call to getAllUsers
    public Collection<User> getAllUsers() {
        if (searchResults != null) {
            return searchResults;
        }
        return adminClient.getAllUsers(Collection.class);
    }

    // Groups for dropdown
    public Collection<GroupMaster> getAllGroups() {
        return adminClient.getAllGroups(Collection.class);
    }

    // ====== Navigation / actions ======
    public String goToAddPage() {
        clear();
        editMode = false;
        passwordDisabled = false;
        return "AdminUserForm.xhtml?faces-redirect=true";
    }

    public String editUser(Integer userId) {
        User u = adminClient.getUserById(User.class, userId.toString());
        if (u == null) {
            return "AdminUserList.xhtml?faces-redirect=true";
        }

        this.id = u.getId();
        this.username = u.getUsername();
        // do NOT load password into form; keep blank and disable in edit
        this.password = null;
        this.fullname = u.getFullname();
        this.email = u.getEmail();
        this.phone = u.getPhone();
        if (u.getGroupid() != null) {
            this.groupId = u.getGroupid().getGroupid();
        } else {
            this.groupId = null;
        }
        this.status = u.getStatus() != null ? u.getStatus() : "Active";

        editMode = true;
        passwordDisabled = true;
        return "AdminUserForm.xhtml?faces-redirect=true";
    }

    public String addUser() {
        // simple server-side checks (you can expand validation)
        if (username == null || username.trim().isEmpty()) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Username required", null));
            return null;
        }
        if (password == null || password.trim().isEmpty()) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Password required", null));
            return null;
        }

        User u = new User();
        u.setUsername(username);
        u.setPassword(password);
        u.setFullname(fullname);
        u.setEmail(email);
        u.setPhone(phone);

        if (groupId != null) {
            GroupMaster gm = new GroupMaster();
            gm.setGroupid(groupId);
            u.setGroupid(gm);
        }

        u.setStatus(status);

        adminClient.addUser(u);
        clear();
        return "AdminUserList.xhtml?faces-redirect=true";
    }

    public String updateUser() {
        if (id == null) {
            return "AdminUserList.xhtml?faces-redirect=true";
        }

        // Fetch existing user to retain fields that we don't overwrite (like hashed password on server)
        User old = adminClient.getUserById(User.class, id.toString());

        User u = new User();
        u.setId(id);
        u.setUsername(username);
        // IMPORTANT: do not send password when editMode is true (user requested)
        // We send password only if a new password is explicitly provided (optional).
        if (password != null && !password.trim().isEmpty()) {
            u.setPassword(password);
        } else {
            // leave password null so backend won't change it — assuming backend handles null accordingly
            u.setPassword(null);
        }

        u.setFullname(fullname);
        u.setEmail(email);
        u.setPhone(phone);

        if (groupId != null) {
            GroupMaster gm = new GroupMaster();
            gm.setGroupid(groupId);
            u.setGroupid(gm);
        } else {
            u.setGroupid(null);
        }

        u.setStatus(status);

        adminClient.updateUser(u, id.toString());

        clear();
        editMode = false;
        return "AdminUserList.xhtml?faces-redirect=true";
    }

    public String deleteUser(Integer userId) {
        adminClient.deleteUser(userId.toString());
        // if you want to show a message:
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("User deleted"));
        return "AdminUserList.xhtml?faces-redirect=true";
    }

    public void cancelEdit() {
        editMode = false;
        clear();
    }

    private void clear() {
        id = null;
        username = "";
        password = "";
        fullname = "";
        email = "";
        phone = "";
        groupId = null;
     status = "Active";
        searchResults = null;
        pageNumber = 1;
        passwordDisabled = false;
    }

    // ===== AJAX checks for uniqueness =====
    public void checkUsernameUnique() {
        if (username == null || username.trim().isEmpty()) {
            return;
        }
        try {
            Boolean exists = adminClient.checkUsername(Boolean.class, username.toString());
            if (Boolean.TRUE.equals(exists)) {
                FacesContext.getCurrentInstance().addMessage("userForm:username", new FacesMessage(FacesMessage.SEVERITY_ERROR, "Username already taken", null));
            }
        } catch (Exception ex) {
            // ignore or log
        }
    }

    public void checkEmailUnique() {
        if (email == null || email.trim().isEmpty()) {
            return;
        }
        try {
            Boolean exists = adminClient.checkEmail(Boolean.class, email.toString());
            if (Boolean.TRUE.equals(exists)) {
                FacesContext.getCurrentInstance().addMessage("userForm:email", new FacesMessage(FacesMessage.SEVERITY_ERROR, "Email already used", null));
            }
        } catch (Exception ex) {
            // ignore or log
        }
    }

    public void checkPhoneUnique() {
        if (phone == null || phone.trim().isEmpty()) {
            return;
        }
        try {
            Boolean exists = adminClient.checkPhone(Boolean.class, phone.toString());
            if (Boolean.TRUE.equals(exists)) {
                FacesContext.getCurrentInstance().addMessage("userForm:phone", new FacesMessage(FacesMessage.SEVERITY_ERROR, "Phone already used", null));
            }
        } catch (Exception ex) {
            // ignore or log
        }
    }

    // ===== Pagination helpers (optional) =====
    public int getTotalPages() {
        int total = getAllUsers().size();
        return (int) Math.ceil((double) total / pageSize);
    }

    public Collection<User> getPagedUsers() {
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

    // ===== GETTERS / SETTERS =====
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

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

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Integer getGroupId() {
        return groupId;
    }

    public void setGroupId(Integer groupId) {
        this.groupId = groupId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public boolean isEditMode() {
        return editMode;
    }

    public void setEditMode(boolean editMode) {
        this.editMode = editMode;
    }

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

    public int getTotalUsers() {
    Collection<User> list = adminClient.getAllUsers(Collection.class);
    return list != null ? list.size() : 0;
}

}
