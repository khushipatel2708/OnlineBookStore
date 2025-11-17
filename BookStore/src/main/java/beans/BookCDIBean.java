package beans;

import Entity.Book;
import Entity.Booktype;
import jakarta.enterprise.context.SessionScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.faces.context.FacesContext;
import jakarta.servlet.http.Part;

import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.io.*;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.*;

@Named("bookBean")
@SessionScoped
public class BookCDIBean implements Serializable {

    private final String BASE_URL = "http://localhost:8080/BookStore/admin";

    private Client client = ClientBuilder.newClient();

    @Inject
    private LoginBean loginBean;

    private Integer id;
    private String bookname;
    private String authorname;
    private Double price;
    private Integer booktypeId;

    private Part coverPhoto;
    private Part frontPagePhoto;
    private Part lastPagePhoto;

    private String coverPreview;
    private String frontPreview;
    private String lastPreview;

    private String searchBookname;
    private String searchAuthor;
    private String searchBooktype;

    private boolean editMode = false;

    // ============ GET ALL BOOKS ================
    public Collection<Book> getAllBooks() {
        return client.target(BASE_URL + "/books/search")
                .queryParam("bookname", searchBookname)
                .queryParam("authorname", searchAuthor)
                .queryParam("booktype", searchBooktype)
                .request(MediaType.APPLICATION_JSON)
                .get(Collection.class);
    }

    public Collection<Booktype> getAllBooktypes() {
        return client.target(BASE_URL + "/booktypes")
                .request(MediaType.APPLICATION_JSON)
                .get(Collection.class);
    }

    public boolean isAdmin() {
        return "Admin".equalsIgnoreCase(loginBean.getRole());
    }

    // ============ HELPERS ================
    private String convertToBase64(Part file) {
        try {
            byte[] bytes = file.getInputStream().readAllBytes();
            return "data:image/png;base64," + Base64.getEncoder().encodeToString(bytes);
        } catch (Exception e) {
            return null;
        }
    }

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
            return null;
        }
    }

    // ================ NAVIGATION ==================
    public String goToAddPage() {
        clear();
        editMode = false;
        return "bookForm.xhtml?faces-redirect=true";
    }

    public String editBook(Integer bookId) {

        Book b = client.target(BASE_URL + "/books/" + bookId)
                .request(MediaType.APPLICATION_JSON)
                .get(Book.class);

        id = b.getId();
        bookname = b.getBookname();
        authorname = b.getAuthorname();
        price = b.getPrice().doubleValue();
        booktypeId = b.getBooktypeId().getId();

        coverPreview = b.getCoverPhoto() != null ? "/uiImages/" + b.getCoverPhoto() : null;
        frontPreview = b.getFrontPagePhoto() != null ? "/uiImages/" + b.getFrontPagePhoto() : null;
        lastPreview = b.getLastPagePhoto() != null ? "/uiImages/" + b.getLastPagePhoto() : null;

        coverPhoto = null;
        frontPagePhoto = null;
        lastPagePhoto = null;

        editMode = true;

        return "bookForm.xhtml?faces-redirect=true";
    }

    // ================== ADD BOOK ====================
    public String addBook() {

        String img1 = saveFile(coverPhoto);
        String img2 = saveFile(frontPagePhoto);
        String img3 = saveFile(lastPagePhoto);

        Map<String, Object> json = new HashMap<>();
        json.put("bookname", bookname);
        json.put("authorname", authorname);
        json.put("price", price);
        json.put("booktypeId", booktypeId);
        json.put("coverPhoto", img1);
        json.put("frontPagePhoto", img2);
        json.put("lastPagePhoto", img3);

        client.target(BASE_URL + "/books")
                .request(MediaType.APPLICATION_JSON)
                .post(Entity.json(json));

        clear();
        return "BookList.xhtml?faces-redirect=true";
    }

    // ================== UPDATE BOOK ====================
    public String updateBook() {

        String img1 = saveFile(coverPhoto);
        String img2 = saveFile(frontPagePhoto);
        String img3 = saveFile(lastPagePhoto);

        if (img1 == null && coverPreview != null)
            img1 = coverPreview.replace("/uiImages/", "");
        if (img2 == null && frontPreview != null)
            img2 = frontPreview.replace("/uiImages/", "");
        if (img3 == null && lastPreview != null)
            img3 = lastPreview.replace("/uiImages/", "");

        Map<String, Object> json = new HashMap<>();
        json.put("bookname", bookname);
        json.put("authorname", authorname);
        json.put("price", price);
        json.put("booktypeId", booktypeId);
        json.put("coverPhoto", img1);
        json.put("frontPagePhoto", img2);
        json.put("lastPagePhoto", img3);

        client.target(BASE_URL + "/books/" + id)
                .request(MediaType.APPLICATION_JSON)
                .put(Entity.json(json));

        editMode = false;
        clear();

        return "BookList.xhtml?faces-redirect=true";
    }

    // ================= DELETE ==================
    public String deleteBook(Integer id) {

        client.target(BASE_URL + "/books/" + id)
                .request()
                .delete();

        return "BookList.xhtml?faces-redirect=true";
    }

    // ================= CANCEL ==================
    public String cancelEdit() {
        clear();
        editMode = false;
        return "BookList.xhtml?faces-redirect=true";
    }

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

    // ========= GETTERS/SETTERS =========

    public Part getCoverPhoto() { return coverPhoto; }
    public void setCoverPhoto(Part p) { this.coverPhoto = p; if (p != null) this.coverPreview = convertToBase64(p); }

    public Part getFrontPagePhoto() { return frontPagePhoto; }
    public void setFrontPagePhoto(Part p) { this.frontPagePhoto = p; if (p != null) this.frontPreview = convertToBase64(p); }

    public Part getLastPagePhoto() { return lastPagePhoto; }
    public void setLastPagePhoto(Part p) { this.lastPagePhoto = p; if (p != null) this.lastPreview = convertToBase64(p); }

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
