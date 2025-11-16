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

    // Preview (Base64 or image path)
    private String coverPreview;
    private String frontPreview;
    private String lastPreview;

    private String searchBookname;
    private String searchAuthor;
    private String searchBooktype;

    private boolean editMode = false;

    // ================= GET ALL ===================
    public String searchBooks() { return null; }
    public String clearSearch() {
        searchBookname = null;
        searchAuthor = null;
        searchBooktype = null;
        return null;
    }

    public Collection<Book> getAllBooks() {
        return admin.searchBooks(searchBookname, searchAuthor, searchBooktype);
    }

    public Collection<Booktype> getAllBooktypes() {
        return admin.getAllBooktypes();
    }

    public boolean isAdmin() {
        return "Admin".equalsIgnoreCase(loginBean.getRole());
    }

    // ================= PREVIEW BASE64 ==================
    private String convertToBase64(Part file) {
        try {
            byte[] bytes = file.getInputStream().readAllBytes();
            return "data:image/png;base64," + Base64.getEncoder().encodeToString(bytes);
        } catch (Exception e) {
            return null;
        }
    }

    // ================= SAVE FILE ==================
    private String saveFile(Part file) {
        if (file == null || file.getSubmittedFileName() == null || file.getSubmittedFileName().isEmpty()) {
            return null;
        }

        try {
            String fileName = System.currentTimeMillis() + "_" + file.getSubmittedFileName();

            String deploymentPath = FacesContext.getCurrentInstance()
                    .getExternalContext()
                    .getRealPath("/uiImages/");

            File uploadDir = new File(deploymentPath);
            if (!uploadDir.exists()) uploadDir.mkdirs();

            Files.copy(file.getInputStream(),
                    new File(uploadDir, fileName).toPath(),
                    StandardCopyOption.REPLACE_EXISTING);

            return fileName;

        } catch (Exception e) {
            System.out.println("Upload Error: " + e.getMessage());
            return null;
        }
    }

    // ================= ADD PAGE ==================
    public String goToAddPage() {
        clear();
        editMode = false;
        return "bookForm.xhtml?faces-redirect=true";
    }

    // ================= EDIT BOOK ==================
    public String editBook(Integer bookId) {

        Book b = admin.findBookById(bookId);

        id = b.getId();
        bookname = b.getBookname();
        authorname = b.getAuthorname();
        price = b.getPrice().doubleValue();
        booktypeId = b.getBooktypeId().getId();

        // IMAGE PREVIEW (existing images)
        coverPreview = (b.getCoverPhoto() != null) ? "/uiImages/" + b.getCoverPhoto() : null;
        frontPreview = (b.getFrontPagePhoto() != null) ? "/uiImages/" + b.getFrontPagePhoto() : null;
        lastPreview = (b.getLastPagePhoto() != null) ? "/uiImages/" + b.getLastPagePhoto() : null;

        coverPhoto = null;
        frontPagePhoto = null;
        lastPagePhoto = null;

        editMode = true;
        return "bookForm.xhtml?faces-redirect=true";
    }

    // ================= ADD BOOK ==================
    public String addBook() {

        String img1 = saveFile(coverPhoto);
        String img2 = saveFile(frontPagePhoto);
        String img3 = saveFile(lastPagePhoto);

        admin.addBook(bookname, authorname, price, booktypeId, img1, img2, img3);

        clear();
        return "BookList.xhtml?faces-redirect=true";
    }

    // ================= UPDATE BOOK ==================
    public String updateBook() {

        String img1 = saveFile(coverPhoto);
        String img2 = saveFile(frontPagePhoto);
        String img3 = saveFile(lastPagePhoto);

        // If no new file uploaded, do not overwrite existing!
        if (img1 == null) img1 = coverPreview.replace("/uiImages/", "");
        if (img2 == null) img2 = frontPreview.replace("/uiImages/", "");
        if (img3 == null) img3 = lastPreview.replace("/uiImages/", "");

        admin.updateBook(id, bookname, authorname, price, booktypeId, img1, img2, img3);

        editMode = false;
        clear();

        return "BookList.xhtml?faces-redirect=true";
    }

    // ================= DELETE ==================
    public String deleteBook(Integer id) {
        admin.deleteBook(id);
        return "BookList.xhtml?faces-redirect=true";
    }

    // ================= CANCEL ==================
    public String cancelEdit() {
        clear();
        editMode = false;
        return "BookList.xhtml?faces-redirect=true";
    }

    // ================= CLEAR ==================
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

    // ================= GET/SET ==================
    public Part getCoverPhoto() { return coverPhoto; }
    public void setCoverPhoto(Part p) {
        this.coverPhoto = p;
        if (p != null) this.coverPreview = convertToBase64(p);
    }

    public Part getFrontPagePhoto() { return frontPagePhoto; }
    public void setFrontPagePhoto(Part p) {
        this.frontPagePhoto = p;
        if (p != null) this.frontPreview = convertToBase64(p);
    }

    public Part getLastPagePhoto() { return lastPagePhoto; }
    public void setLastPagePhoto(Part p) {
        this.lastPagePhoto = p;
        if (p != null) this.lastPreview = convertToBase64(p);
    }

    public String getCoverPreview() { return coverPreview; }
    public String getFrontPreview() { return frontPreview; }
    public String getLastPreview() { return lastPreview; }

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getBookname() { return bookname; }
    public void setBookname(String bookname) { this.bookname = bookname; }

    public String getAuthorname() { return authorname; }
    public void setAuthorname(String authorname) { this.authorname = authorname; }

    public Double getPrice() { return price; }
    public void setPrice(Double price) { this.price = price; }

    public Integer getBooktypeId() { return booktypeId; }
    public void setBooktypeId(Integer bt) { this.booktypeId = bt; }

    public boolean isEditMode() { return editMode; }
    public void setEditMode(boolean editMode) { this.editMode = editMode; }

    public String getSearchBookname() { return searchBookname; }
    public void setSearchBookname(String s) { this.searchBookname = s; }

    public String getSearchAuthor() { return searchAuthor; }
    public void setSearchAuthor(String s) { this.searchAuthor = s; }

    public String getSearchBooktype() { return searchBooktype; }
    public void setSearchBooktype(String s) { this.searchBooktype = s; }
}
