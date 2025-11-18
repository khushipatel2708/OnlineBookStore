package beans;

import Entity.Booktype;
import client.MyAdminClient;
import jakarta.enterprise.context.SessionScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import java.io.Serializable;
import java.util.Collection;

@Named("booktypeBean")
@SessionScoped
public class BooktypeCDIBean implements Serializable {

    private MyAdminClient adminClient = new MyAdminClient();

    @Inject
    private LoginBean loginBean;

    private Integer id;
    private String type;
    private String description;
    private boolean editMode = false;

    // ================= ROLE CHECK =================
    public boolean isAdmin() {
        return "Admin".equalsIgnoreCase(loginBean.getRole());
    }

    // ===================== GET ALL BOOKTYPES =======================
    public Collection<Booktype> getAllBooktypes() {
        if (!isAdmin()) return null;
        try {
            return adminClient.getAllBooktypes(Collection.class);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // ===================== ADD BOOKTYPE ============================
    public String addBooktype() {
        if (!isAdmin()) return null;
        Booktype bt = new Booktype();
        bt.setType(type);
        bt.setDescription(description);

        try {
            adminClient.addBooktype(bt);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

        type = "";
        description = "";
        return null;
    }

    // ===================== LOAD BOOKTYPE FOR EDIT ===================
    public String editBooktype(Integer btId) {
        try {
            Booktype bt = adminClient.getBooktypeById(Booktype.class, btId.toString());
            this.id = bt.getId();
            this.type = bt.getType();
            this.description = bt.getDescription();
            this.editMode = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    // ===================== UPDATE BOOKTYPE ==========================
    public String updateBooktype() {
        if (!isAdmin()) return null;

        Booktype bt = new Booktype();
        bt.setId(id); // important to set the ID for update
        bt.setType(type);
        bt.setDescription(description);

        try {
            adminClient.updateBooktype(bt, id.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }

        editMode = false;
        type = "";
        description = "";
        return null;
    }

    // ===================== CANCEL EDIT ==============================
    public void cancelEdit() {
        editMode = false;
        type = "";
        description = "";
    }

    // ===================== DELETE BOOKTYPE ==========================
    public String deleteBooktype(Integer btId) {
        if (!isAdmin()) return null;
        try {
            adminClient.deleteBooktype(btId.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    // ===================== GETTERS / SETTERS ========================
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public boolean isEditMode() { return editMode; }
    public void setEditMode(boolean editMode) { this.editMode = editMode; }
}
