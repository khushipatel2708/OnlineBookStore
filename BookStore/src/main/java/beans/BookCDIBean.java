package beans;

import Entity.Book;
import Entity.Booktype;
import client.MyAdminClient;
import jakarta.enterprise.context.SessionScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.servlet.http.Part;

import java.io.File;
import java.io.Serializable;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.Base64;
import java.util.Collection;

@Named("bookBean")
@SessionScoped
public class BookCDIBean implements Serializable {

    private MyAdminClient adminClient = new MyAdminClient(); // USE CLIENT ONLY

    @Inject
    private LoginBean loginBean;

    private Integer id;
    private String bookname;
    private String authorname;
    private BigDecimal price;
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

    // ===== FILE STORAGE PATH =====
    private final String uploadFolder = System.getProperty("user.home") + File.separator + "BookStoreUploads";

    // ===== ROLE CHECK =====
    public boolean isAdmin() {
        return "Admin".equalsIgnoreCase(loginBean.getRole());
    }

    // ===== GET ALL BOOKS =====
    public Collection<Book> getAllBooks() {
        if (!isAdmin()) return null;
        return adminClient.getAllBooks(Collection.class);
    }

    // ===== GET ALL BOOK TYPES =====
    public Collection<Booktype> getAllBooktypes() {
        if (!isAdmin()) return null;
        return adminClient.getAllBooktypes(Collection.class);
    }

    // ===== HELPER METHODS =====
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
            File uploadDir = new File(uploadFolder);
            if (!uploadDir.exists()) uploadDir.mkdirs();

            Files.copy(file.getInputStream(),
                    new File(uploadDir, fileName).toPath(),
                    StandardCopyOption.REPLACE_EXISTING);

            return fileName;

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // ===== NAVIGATION =====
    public String goToAddPage() {
        clear();
        editMode = false;
        return "bookForm.xhtml?faces-redirect=true";
    }

    // ===== EDIT BOOK =====
    public String editBook(Integer bookId) {
        Book b = adminClient.getBookById(Book.class, bookId.toString());

        this.id = b.getId();
        this.bookname = b.getBookname();
        this.authorname = b.getAuthorname();
        this.price = b.getPrice();
        this.booktypeId = b.getBooktypeId().getId();

        coverPreview = (b.getCoverPhoto() != null) ? getFilePath(b.getCoverPhoto()) : null;
        frontPreview = (b.getFrontPagePhoto() != null) ? getFilePath(b.getFrontPagePhoto()) : null;
        lastPreview = (b.getLastPagePhoto() != null) ? getFilePath(b.getLastPagePhoto()) : null;

        coverPhoto = null;
        frontPagePhoto = null;
        lastPagePhoto = null;

        editMode = true;
        return "bookForm.xhtml?faces-redirect=true";
    }

    private String getFilePath(String fileName) {
        return "/BookStoreUploads/" + fileName;
    }

    // ===== ADD BOOK =====
    public String addBook() {
        String img1 = saveFile(coverPhoto);
        String img2 = saveFile(frontPagePhoto);
        String img3 = saveFile(lastPagePhoto);

        Book b = new Book();
        b.setBookname(bookname);
        b.setAuthorname(authorname);
        b.setPrice(price);

        Booktype bt = new Booktype();
        bt.setId(booktypeId);
        b.setBooktypeId(bt);

        b.setCoverPhoto(img1);
        b.setFrontPagePhoto(img2);
        b.setLastPagePhoto(img3);

        adminClient.addBook(b); // POST

        clear();
        return "BookList.xhtml?faces-redirect=true";
    }

    // ===== UPDATE BOOK =====
    public String updateBook() {
        String img1 = saveFile(coverPhoto);
        String img2 = saveFile(frontPagePhoto);
        String img3 = saveFile(lastPagePhoto);

        if (img1 == null && coverPreview != null)
            img1 = new File(coverPreview).getName();
        if (img2 == null && frontPreview != null)
            img2 = new File(frontPreview).getName();
        if (img3 == null && lastPreview != null)
            img3 = new File(lastPreview).getName();

        Book b = new Book();
        b.setBookname(bookname);
        b.setAuthorname(authorname);
        b.setPrice(price);

        Booktype bt = new Booktype();
        bt.setId(booktypeId);
        b.setBooktypeId(bt);

        b.setCoverPhoto(img1);
        b.setFrontPagePhoto(img2);
        b.setLastPagePhoto(img3);

        adminClient.updateBook(b, id.toString()); // PUT

        editMode = false;
        clear();
        return "BookList.xhtml?faces-redirect=true";
    }

    // ===== DELETE BOOK =====
    public String deleteBook(Integer bookId) {
        adminClient.deleteBook(bookId.toString());
        return "BookList.xhtml?faces-redirect=true";
    }

    // ===== CANCEL EDIT =====
    public void cancelEdit() {
        editMode = false;
        clear();
    }

    // ===== CLEAR FIELDS =====
    private void clear() {
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

    // ===== GETTERS & SETTERS =====
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getBookname() { return bookname; }
    public void setBookname(String bookname) { this.bookname = bookname; }

    public String getAuthorname() { return authorname; }
    public void setAuthorname(String authorname) { this.authorname = authorname; }

    public BigDecimal getPrice() { return price; }
    public void setPrice(BigDecimal price) { this.price = price; }

    public Integer getBooktypeId() { return booktypeId; }
    public void setBooktypeId(Integer booktypeId) { this.booktypeId = booktypeId; }

    public boolean isEditMode() { return editMode; }
    public void setEditMode(boolean editMode) { this.editMode = editMode; }

    public Part getCoverPhoto() { return coverPhoto; }
    public void setCoverPhoto(Part coverPhoto) {
        this.coverPhoto = coverPhoto;
        if (coverPhoto != null) this.coverPreview = convertToBase64(coverPhoto);
    }

    public Part getFrontPagePhoto() { return frontPagePhoto; }
    public void setFrontPagePhoto(Part frontPagePhoto) {
        this.frontPagePhoto = frontPagePhoto;
        if (frontPagePhoto != null) this.frontPreview = convertToBase64(frontPagePhoto);
    }

    public Part getLastPagePhoto() { return lastPagePhoto; }
    public void setLastPagePhoto(Part lastPagePhoto) {
        this.lastPagePhoto = lastPagePhoto;
        if (lastPagePhoto != null) this.lastPreview = convertToBase64(lastPagePhoto);
    }

    public String getCoverPreview() { return coverPreview; }
    public String getFrontPreview() { return frontPreview; }
    public String getLastPreview() { return lastPreview; }

    public String getSearchBookname() { return searchBookname; }
    public void setSearchBookname(String searchBookname) { this.searchBookname = searchBookname; }

    public String getSearchAuthor() { return searchAuthor; }
    public void setSearchAuthor(String searchAuthor) { this.searchAuthor = searchAuthor; }

    public String getSearchBooktype() { return searchBooktype; }
    public void setSearchBooktype(String searchBooktype) { this.searchBooktype = searchBooktype; }
}
