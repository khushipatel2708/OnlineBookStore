package beans;

import Entity.Book;
import Entity.Booktype;
import Entity.Cart;
import Entity.User;
import client.MyAdminClient;
import jakarta.enterprise.context.SessionScoped;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.servlet.http.Part;

import java.io.File;
import java.io.Serializable;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@Named("bookBean")
@SessionScoped
public class BookCDIBean implements Serializable {

    private MyAdminClient adminClient = new MyAdminClient();

    @Inject
    private LoginBean loginBean;

    private Integer id;
    private String bookname;
    private String authorname;
    private BigDecimal price;
    private Integer booktypeId;
     private Integer available;

    private Part coverPhoto;
    private Part frontPagePhoto;
    private Part lastPagePhoto;

    // preview values for showing existing images (these will be URL paths like "/BookStore/BookStoreUploads/filename.jpg")
    private String coverPreview;
    private String frontPreview;
    private String lastPreview;

    private String searchBookname;
    private String searchAuthor;
    private String searchBooktype;
    private Book selectedBook;
    private Collection<Book> searchResults = null;

    private boolean editMode = false;

    // storage folder on server (must match servlet)
    private final String uploadFolder = System.getProperty("user.home") + File.separator + "BookStoreUploads";

    public boolean isAdmin() {
        return "Admin".equalsIgnoreCase(loginBean.getRole());
    }

    public Book getSelectedBook() {
        return selectedBook;
    }

// Override to return either all books or filtered
    public Collection<Book> getAllBooks() {
        if (searchResults != null) {
            return searchResults;
        }
        return adminClient.getAllBooks(Collection.class);
    }

    // ========== SEARCH ==========
    public String searchBooks() {

        searchResults = adminClient.searchBooks(
                Collection.class,
                searchAuthor,
                searchBooktype,
                searchBookname
        );

        return null; // stay on same page
    }

    public String clearSearch() {
        searchAuthor = "";
        searchBookname = "";
        searchBooktype = "";
        searchResults = null;
        return null;
    }

    public Collection<Booktype> getAllBooktypes() {
        return adminClient.getAllBooktypes(Collection.class);
    }

    // Save file to uploadFolder; return safe filename or null
    private String saveFile(Part file) {
        try {
            if (file == null) {
                return null;
            }
            String submitted = file.getSubmittedFileName();
            if (submitted == null || submitted.trim().isEmpty()) {
                return null;
            }

            // sanitize filename: replace spaces and remove path parts
            String baseName = new File(submitted).getName().replaceAll("\\s+", "_");

            String fileName = System.currentTimeMillis() + "_" + baseName;

            File dir = new File(uploadFolder);
            if (!dir.exists()) {
                dir.mkdirs();
            }

            File dest = new File(dir, fileName);
            Files.copy(file.getInputStream(), dest.toPath(), StandardCopyOption.REPLACE_EXISTING);
            return fileName;
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    // helper to produce the public URL path used in JSF (matches servlet mapping)
    private String makePublicPath(String filename) {
        if (filename == null) {
            return null;
        }
        // this is the path that will be requested and handled by FileServlet
        return "/BookStore/BookStoreUploads/" + filename;
    }

    public String goToAddPage() {
        clear();
        editMode = false;
        return "bookForm.xhtml?faces-redirect=true";
    }

    public String editBook(Integer bookId) {
        Book b = adminClient.getBookById(Book.class, bookId.toString());
        if (b == null) {
            return "BookList.xhtml?faces-redirect=true";
        }

        this.id = b.getId();
        this.bookname = b.getBookname();
        this.authorname = b.getAuthorname();
        this.price = b.getPrice();
        this.available=b.getAvailable();
        if (b.getBooktypeId() != null) {
            this.booktypeId = b.getBooktypeId().getId();
        }

        // show public URL (handled by FileServlet)
        this.coverPreview = (b.getCoverPhoto() != null) ? makePublicPath(b.getCoverPhoto()) : null;
        this.frontPreview = (b.getFrontPagePhoto() != null) ? makePublicPath(b.getFrontPagePhoto()) : null;
        this.lastPreview = (b.getLastPagePhoto() != null) ? makePublicPath(b.getLastPagePhoto()) : null;

        this.coverPhoto = null;
        this.frontPagePhoto = null;
        this.lastPagePhoto = null;

        editMode = true;
        return "bookForm.xhtml?faces-redirect=true";
    }

    public String addBook() {
        String img1 = saveFile(coverPhoto);
        String img2 = saveFile(frontPagePhoto);
        String img3 = saveFile(lastPagePhoto);

        Book b = new Book();
        b.setBookname(bookname);
        b.setAuthorname(authorname);
        b.setPrice(price);
        b.setAvailable(available);

        Booktype bt = new Booktype();
        bt.setId(booktypeId);
        b.setBooktypeId(bt);

        b.setCoverPhoto(img1);
        b.setFrontPagePhoto(img2);
        b.setLastPagePhoto(img3);

        adminClient.addBook(b);
        clear();
        return "BookList.xhtml?faces-redirect=true";
    }

    public String updateBook() {
        // get existing filenames from server (to keep when user doesn't upload new)
        Book old = adminClient.getBookById(Book.class, id.toString());
        String oldCover = (old != null) ? old.getCoverPhoto() : null;
        String oldFront = (old != null) ? old.getFrontPagePhoto() : null;
        String oldLast = (old != null) ? old.getLastPagePhoto() : null;

        String img1 = saveFile(coverPhoto);
        String img2 = saveFile(frontPagePhoto);
        String img3 = saveFile(lastPagePhoto);

        if (img1 == null) {
            img1 = oldCover;
        }
        if (img2 == null) {
            img2 = oldFront;
        }
        if (img3 == null) {
            img3 = oldLast;
        }

        Book b = new Book();
        b.setBookname(bookname);
        b.setAuthorname(authorname);
        b.setPrice(price);
  b.setAvailable(available);
        Booktype bt = new Booktype();
        bt.setId(booktypeId);
        b.setBooktypeId(bt);

        b.setCoverPhoto(img1);
        b.setFrontPagePhoto(img2);
        b.setLastPagePhoto(img3);

        adminClient.updateBook(b, id.toString());
        clear();
        editMode = false;
        return "BookList.xhtml?faces-redirect=true";
    }

    public String deleteBook(Integer bookId) {
        adminClient.deleteBook(bookId.toString());
        return "BookList.xhtml?faces-redirect=true";
    }

    public void cancelEdit() {
        editMode = false;
        clear();
    }

    private void clear() {
        id = null;
        bookname = "";
        authorname = "";
        price = null;
        booktypeId = null;
         available=null;

        coverPhoto = null;
        frontPagePhoto = null;
        lastPagePhoto = null;

        coverPreview = null;
        frontPreview = null;
        lastPreview = null;
    }

    public String viewDetails() {
        // Get the request parameter
        Map<String, String> params = FacesContext.getCurrentInstance()
                .getExternalContext()
                .getRequestParameterMap();
        String bookIdStr = params.get("bookId");

        if (bookIdStr != null) {
            Integer id = Integer.valueOf(bookIdStr);
            selectedBook = adminClient.getBookById(Book.class, id.toString());
            return "ViewDetails.xhtml?faces-redirect=true";
        }
        return null; // stay on same page if id is null
    }

    // ===== GETTERS / SETTERS =====
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

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }
     public Integer getAvailable() {
        return available;
    }

    public void setAvailable(Integer available) {
        this.available = available;
    }


    public Integer getBooktypeId() {
        return booktypeId;
    }

    public void setBooktypeId(Integer booktypeId) {
        this.booktypeId = booktypeId;
    }

    public Part getCoverPhoto() {
        return coverPhoto;
    }

    public void setCoverPhoto(Part coverPhoto) {
        this.coverPhoto = coverPhoto;
    }

    public Part getFrontPagePhoto() {
        return frontPagePhoto;
    }

    public void setFrontPagePhoto(Part frontPagePhoto) {
        this.frontPagePhoto = frontPagePhoto;
    }

    public Part getLastPagePhoto() {
        return lastPagePhoto;
    }

    public void setLastPagePhoto(Part lastPagePhoto) {
        this.lastPagePhoto = lastPagePhoto;
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

    public boolean isEditMode() {
        return editMode;
    }

    public void setEditMode(boolean editMode) {
        this.editMode = editMode;
    }

    public String getSearchBookname() {
        return searchBookname;
    }

    public void setSearchBookname(String searchBookname) {
        this.searchBookname = searchBookname;
    }

    public String getSearchAuthor() {
        return searchAuthor;
    }

    public void setSearchAuthor(String searchAuthor) {
        this.searchAuthor = searchAuthor;
    }

    public String getSearchBooktype() {
        return searchBooktype;
    }

    public void setSearchBooktype(String searchBooktype) {
        this.searchBooktype = searchBooktype;
    }
    
    private int pageSize = 10;      // Number of rows per page
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
        int totalGroups = getAllBooks().size();
        return (int) Math.ceil((double) totalGroups / pageSize);
    }

    public Collection<Book> getPagedGroups() {
        List<Book> all = new ArrayList<>(getAllBooks());
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
