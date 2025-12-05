package beans;

import Entity.GroupMaster;
import client.MyAdminClient;
import jakarta.enterprise.context.SessionScoped;
import jakarta.inject.Named;
import java.io.Serializable;
import java.util.Collection;

@Named("groupBean")
@SessionScoped
public class GroupBean implements Serializable {

    private MyAdminClient adminClient = new MyAdminClient();

    private Integer id;
    private String groupname;
    private String username; // NEW FIELD
    private boolean editMode = false;

    // ---------- GET ALL GROUPS ----------
    public Collection<GroupMaster> getAllGroups() {
        return adminClient.getAllGroups(Collection.class);
    }

    // ---------- RESET FORM ----------
    public String resetForm() {
        id = null;
        groupname = "";
        username = ""; // RESET username
        editMode = false;
        return null;
    }

    // ---------- ADD GROUP ----------
    public String addGroup() {
        GroupMaster g = new GroupMaster();
        g.setGroupname(groupname);
        g.setUsername(username); // SET username

        adminClient.addGroup(g);

        resetForm();
        return "/group.xhtml?faces-redirect=true";
    }

    // ---------- LOAD GROUP FOR EDIT ----------
    public String editGroup(Integer groupId) {
        GroupMaster gm = adminClient.getGroupById(GroupMaster.class, groupId.toString());

        this.id = gm.getGroupid();
        this.groupname = gm.getGroupname();
        this.username = gm.getUsername(); // LOAD username
        this.editMode = true;

        return null;
    }

    // ---------- UPDATE GROUP ----------
    public String updateGroup() {
        GroupMaster g = new GroupMaster();
        g.setGroupid(id);
        g.setGroupname(groupname);
        g.setUsername(username); // UPDATE username

        adminClient.updateGroup(g, id.toString());

        resetForm();
        return "/group.xhtml?faces-redirect=true";
    }

    // ---------- SINGLE SAVE METHOD ----------
    public String saveGroup() {
        if (editMode) {
            return updateGroup();
        } else {
            return addGroup();
        }
    }

    // ---------- DELETE GROUP ----------
    public String deleteGroup(Integer groupId) {
        adminClient.deleteGroup(groupId.toString());
        return "/group.xhtml?faces-redirect=true";
    }

    // ---------- GETTERS & SETTERS ----------
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getGroupname() { return groupname; }
    public void setGroupname(String groupname) { this.groupname = groupname; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public boolean isEditMode() { return editMode; }
    public void setEditMode(boolean editMode) { this.editMode = editMode; }
}
