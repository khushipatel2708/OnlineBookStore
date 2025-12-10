package beans;

import Entity.Booktype;
import client.MyAdminClient;
import jakarta.enterprise.context.SessionScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

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
      public int getTotalBooktype() {
    Collection<Booktype> list = adminClient.getAllBooktypes(Collection.class);
    return list != null ? list.size() : 0;
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
        int totalGroups = getAllBooktypes().size();
        return (int) Math.ceil((double) totalGroups / pageSize);
    }

    public Collection<Booktype> getPagedGroups() {
        List<Booktype> all = new ArrayList<>(getAllBooktypes());
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
