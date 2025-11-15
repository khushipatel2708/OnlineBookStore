package beans;

import EJB.AdminSessionBeanLocal;
import Entity.Booktype;
import jakarta.ejb.EJB;
import jakarta.enterprise.context.SessionScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import java.io.Serializable;
import java.util.Collection;

@Named("booktypeBean")
@SessionScoped
public class BooktypeCDIBean implements Serializable {

    @EJB
    private AdminSessionBeanLocal admin;

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

    // FETCH ALL
    public Collection<Booktype> getAllBooktypes() {
        if (!isAdmin()) return null;
        return admin.getAllBooktypes();
    }

    // ADD
    public String addBooktype() {
        admin.addBooktype(type, description);
        type = "";
        description = "";
        return null;
    }

    // LOAD FOR EDIT
    public String editBooktype(Integer btId) {
        Booktype bt = admin.getBooktypeById(btId);
        this.id = bt.getId();
        this.type = bt.getType();
        this.description = bt.getDescription();
        this.editMode = true;
        return null;
    }

    // UPDATE
    public String updateBooktype() {
        admin.updateBooktype(id, type, description);
        editMode = false;
        type = "";
        description = "";
        return null;
    }

    // CANCEL
    public void cancelEdit() {
        editMode = false;
        type = "";
        description = "";
    }

    // DELETE
    public String deleteBooktype(Integer btId) {
        admin.deleteBooktype(btId);
        return null;
    }

    // GETTERS & SETTERS
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public boolean isEditMode() { return editMode; }
    public void setEditMode(boolean editMode) { this.editMode = editMode; }
}
