package beans;

import EJB.AdminSessionBeanLocal;
import Entity.Book;
import Entity.Booktype;
import jakarta.ejb.EJB;
import jakarta.enterprise.context.SessionScoped;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.servlet.http.Part;

import java.io.*;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.Base64;
import java.util.Collection;

@Named("bookBean")
@SessionScoped
public class BookCDIBean implements Serializable {

    @EJB
    private AdminSessionBeanLocal admin;

    @Inject
    private LoginBean loginBean;

    private Integer id;
    private String bookname;
    private String authorname;
    private Double price;
    private Integer booktypeId;

    // File upload fields
    private Part coverPhoto;
    private Part frontPagePhoto;
    private Part lastPagePhoto;

    // For UI preview only
    private String coverPreview;
    private String frontPreview;
    private String lastPreview;

    private String searchBookname;
    private String searchAuthor;
    private String searchBooktype;

    private boolean editMode = false;

    // ================= GET ALL DATA ===================
  // ✅ ADD THESE TWO METHODS HERE
    public String searchBooks() {
        return null;  // refresh same page
    }

    public String clearSearch() {
        searchBookname = null;
        searchAuthor = null;
        searchBooktype = null;
        return null;
    }

    // your existing methods...
    public Collection<Book> getAllBooks() {
        return admin.searchBooks(searchBookname, searchAuthor, searchBooktype);
    }
   

    public Collection<Booktype> getAllBooktypes() {
        return admin.getAllBooktypes();
    }

    public boolean isAdmin() {
        return "Admin".equalsIgnoreCase(loginBean.getRole());
    }

    // ================== BASE64 PREVIEW ===================
    private String convertToBase64(Part file) {
        try {
            byte[] bytes = file.getInputStream().readAllBytes();
            return "data:image/png;base64," + Base64.getEncoder().encodeToString(bytes);
        } catch (Exception e) {
            return null;
        }
    }

    // ================== SAVE FILE ====================
    private String saveFile(Part file) {
        if (file == null || file.getSubmittedFileName() == null) {
            return null;
        }

        try {
            String fileName = System.currentTimeMillis() + "_" + file.getSubmittedFileName();

            // Get real deployment path
            String deploymentPath
                    = FacesContext.getCurrentInstance()
                            .getExternalContext()
                            .getRealPath("/uiImages/");

            File uploadDir = new File(deploymentPath);
            if (!uploadDir.exists()) {
                uploadDir.mkdirs();
            }

            Files.copy(file.getInputStream(),
                    new File(uploadDir, fileName).toPath(),
                    StandardCopyOption.REPLACE_EXISTING);

            return fileName;

        } catch (Exception e) {
            System.out.println("File Upload Error: " + e.getMessage());
            return null;
        }
    }

    // ================== ADD PAGE REDIRECT ==================
    public String goToAddPage() {
        clear();
        editMode = false;
        return "bookForm.xhtml?faces-redirect=true";
    }

    // ================== EDIT BOOK ==================
   public String editBook(Integer bookId) {

    Book b = admin.findBookById(bookId);

    id = b.getId();
    bookname = b.getBookname();
    authorname = b.getAuthorname();
    price = b.getPrice().doubleValue();
    booktypeId = b.getBooktypeId().getId();

    // Load old images for preview
    coverPreview = (b.getCoverPhoto() != null) ? "/uiImages/" + b.getCoverPhoto() : null;
    frontPreview = (b.getFrontPagePhoto() != null) ? "/uiImages/" + b.getFrontPagePhoto() : null;
    lastPreview = (b.getLastPagePhoto() != null) ? "/uiImages/" + b.getLastPagePhoto() : null;

    // No new file uploaded yet
    coverPhoto = null;
    frontPagePhoto = null;
    lastPagePhoto = null;

    editMode = true;

    return "bookForm.xhtml?faces-redirect=true";
}


    // ================== ADD BOOK ==================
    public String addBook() {

        String img1 = saveFile(coverPhoto);
        String img2 = saveFile(frontPagePhoto);
        String img3 = saveFile(lastPagePhoto);

        admin.addBook(bookname, authorname, price, booktypeId, img1, img2, img3);

        clear();
        return "BookList.xhtml?faces-redirect=true";
    }

    // ================== UPDATE BOOK ==================
    public String updateBook() {

        // check only new images uploaded
        String img1 = saveFile(coverPhoto);
        String img2 = saveFile(frontPagePhoto);
        String img3 = saveFile(lastPagePhoto);

        admin.updateBook(id, bookname, authorname, price, booktypeId, img1, img2, img3);

        editMode = false;
        clear();

        return "BookList.xhtml?faces-redirect=true";
    }

    // ================== DELETE BOOK ==================
    public String deleteBook(Integer id) {
        admin.deleteBook(id);
        return "BookList.xhtml?faces-redirect=true";
    }

    // ================== CANCEL EDIT ==================
    public String cancelEdit() {
        clear();
        editMode = false;
        return "BookList.xhtml?faces-redirect=true";
    }

    // ================== CLEAR FIELDS ==================
    public void clear() {
        id = null;
        bookname = "";
        authorname = "";
        price = null;
        booktypeId = null;

        coverPhoto = null;
        frontPagePhoto = null;
        lastPagePhoto = null;

        coverPreview = null;
        frontPreview = null;
        lastPreview = null;
    }

    // ================== GETTERS/SETTERS ==================
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getBookname() {
        return bookname;
    }

    public void setBookname(String bookname) {
        this.bookname = bookname;
    }

    public String getAuthorname() {
        return authorname;
    }

    public void setAuthorname(String authorname) {
        this.authorname = authorname;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Integer getBooktypeId() {
        return booktypeId;
    }

    public void setBooktypeId(Integer booktypeId) {
        this.booktypeId = booktypeId;
    }

    public boolean isEditMode() {
        return editMode;
    }

    public void setEditMode(boolean editMode) {
        this.editMode = editMode;
    }

    // ========== Photo Setters With Preview ==========
    public Part getCoverPhoto() {
        return coverPhoto;
    }

    public void setCoverPhoto(Part coverPhoto) {
        this.coverPhoto = coverPhoto;
        if (coverPhoto != null) {
            this.coverPreview = convertToBase64(coverPhoto);
        }
    }

    public Part getFrontPagePhoto() {
        return frontPagePhoto;
    }

    public void setFrontPagePhoto(Part frontPagePhoto) {
        this.frontPagePhoto = frontPagePhoto;
        if (frontPagePhoto != null) {
            this.frontPreview = convertToBase64(frontPagePhoto);
        }
    }

    public Part getLastPagePhoto() {
        return lastPagePhoto;
    }

    public void setLastPagePhoto(Part lastPagePhoto) {
        this.lastPagePhoto = lastPagePhoto;
        if (lastPagePhoto != null) {
            this.lastPreview = convertToBase64(lastPagePhoto);
        }
    }

    public String getCoverPreview() {
        return coverPreview;
    }

    public String getFrontPreview() {
        return frontPreview;
    }

    public String getLastPreview() {
        return lastPreview;
    }

    public String getSearchBookname() {
        return searchBookname;
    }

    public void setSearchBookname(String s) {
        this.searchBookname = s;
    }

    public String getSearchAuthor() {
        return searchAuthor;
    }

    public void setSearchAuthor(String s) {
        this.searchAuthor = s;
    }

    public String getSearchBooktype() {
        return searchBooktype;
    }

    public void setSearchBooktype(String s) {
        this.searchBooktype = s;
    }

}
