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

    // ROLE CHECK
    public boolean isAdmin() {
        return "Admin".equalsIgnoreCase(loginBean.getRole());
    }

    // GET ALL BOOK TYPES
    public Collection<Booktype> getAllBooktypes() {
        if (!isAdmin()) return null;
        return adminClient.getAllBooktypes(Collection.class);
    }

    // RESET FORM
    public String resetForm() {
        id = null;
        type = "";
        description = "";
        editMode = false;
        return null;
    }

    // ADD
    public String addBooktype() {
        if (!isAdmin()) return null;

        Booktype bt = new Booktype();
        bt.setType(type);
        bt.setDescription(description);

        adminClient.addBooktype(bt);
        resetForm();

        return "/Booktype.xhtml?faces-redirect=true";
    }

    // LOAD FOR EDIT
    public String editBooktype(Integer btId) {
        Booktype bt = adminClient.getBooktypeById(Booktype.class, btId.toString());
        this.id = bt.getId();
        this.type = bt.getType();
        this.description = bt.getDescription();
        this.editMode = true;
        return null;
    }

    // UPDATE
    public String updateBooktype() {
        if (!isAdmin()) return null;

        Booktype bt = new Booktype();
        bt.setId(id);
        bt.setType(type);
        bt.setDescription(description);

        adminClient.updateBooktype(bt, id.toString());
        resetForm();

        return "/Booktype.xhtml?faces-redirect=true";
    }

    // CANCEL
    public void cancelEdit() {
        resetForm();
    }

    // DELETE
    public String deleteBooktype(Integer btId) {
        if (!isAdmin()) return null;

        adminClient.deleteBooktype(btId.toString());
        return null;
    }

    // GETTERS / SETTERS
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public boolean isEditMode() { return editMode; }
    public void setEditMode(boolean editMode) { this.editMode = editMode; }
}
