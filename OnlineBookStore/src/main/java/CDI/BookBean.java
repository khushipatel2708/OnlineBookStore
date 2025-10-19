package CDI;

import EJB.BookSessionBeanLocal;
import Entity.Book;
import Entity.Booktype;
import jakarta.ejb.EJB;
import jakarta.enterprise.context.SessionScoped;
import jakarta.inject.Named;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.servlet.http.Part;

import java.io.*;
import java.nio.file.Files;
import java.io.Serializable;
import java.util.List;

@Named(value = "bookBean")
@SessionScoped
public class BookBean implements Serializable {

    @EJB
    private BookSessionBeanLocal bookSessionBean;

    private int bookId;
    private int booktypeId;
    private String bookname;
    private String authorname;
    private double price;
    private boolean available = true;

    private Part coverPhoto;
    private Part frontPhoto;
    private Part lastPhoto;

    private final String IMAGE_FOLDER = "D:/Project/OnlineBookStore/OnlineBookStore/images";

    private String coverPhotoName;
    private String frontPhotoName;
    private String lastPhotoName;

    // --- Save or Update Book ---
    public String saveBook() {
        FacesContext context = FacesContext.getCurrentInstance();
        try {
            File folder = new File(IMAGE_FOLDER);
            if (!folder.exists()) folder.mkdirs();

            Booktype bt = new Booktype();
            bt.setId(booktypeId);

            Book book;
            if (bookId > 0) { // Edit mode
                book = bookSessionBean.getBookById(bookId);
                if (book == null) {
                    context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Book not found!", null));
                    return null;
                }
            } else { // Add mode
                book = new Book();
            }

            book.setBooktype(bt);
            book.setBookname(bookname);
            book.setAuthorname(authorname);
            book.setPrice(price);
            book.setAvailable(available);

            // Save uploaded images (keep old if not replaced)
            if (coverPhoto != null) book.setCover_photo(saveFile(coverPhoto));
            else if (bookId > 0) book.setCover_photo(coverPhotoName);

            if (frontPhoto != null) book.setFront_page_photo(saveFile(frontPhoto));
            else if (bookId > 0) book.setFront_page_photo(frontPhotoName);

            if (lastPhoto != null) book.setLast_page_photo(saveFile(lastPhoto));
            else if (bookId > 0) book.setLast_page_photo(lastPhotoName);

            if (bookId > 0)
                bookSessionBean.updateBook(book);
            else
                bookSessionBean.addBook(book);

            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Book saved successfully!", null));

            clearFields();
            return "bookList2.xhtml?faces-redirect=true";

        } catch (Exception e) {
            e.printStackTrace();
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error: " + e.getMessage(), null));
            return null;
        }
    }

    // --- Delete Book ---
    public void deleteBook(int id) {
        bookSessionBean.deleteBook(id);
        FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_INFO, "Book deleted successfully!", null));
    }

    // --- Load book for edit ---
    public String loadBook(int id) {
        Book book = bookSessionBean.getBookById(id);
        if (book != null) {
            bookId = book.getId();
            booktypeId = book.getBooktype().getId();
            bookname = book.getBookname();
            authorname = book.getAuthorname();
            price = book.getPrice();
            available = book.isAvailable();

            coverPhotoName = book.getCover_photo();
            frontPhotoName = book.getFront_page_photo();
            lastPhotoName = book.getLast_page_photo();
        }
        return "bookForm.xhtml?faces-redirect=true";
    }

    // --- Save file method ---
    private String saveFile(Part filePart) throws IOException {
        String fileName = System.currentTimeMillis() + "_" + filePart.getSubmittedFileName();
        File file = new File(IMAGE_FOLDER, fileName);
        try (InputStream input = filePart.getInputStream()) {
            Files.copy(input, file.toPath(), java.nio.file.StandardCopyOption.REPLACE_EXISTING);
        }
        return fileName;
    }

    private void clearFields() {
        bookId = 0;
        booktypeId = 0;
        bookname = authorname = null;
        price = 0.0;
        available = true;
        coverPhoto = frontPhoto = lastPhoto = null;
        coverPhotoName = frontPhotoName = lastPhotoName = null;
    }

    // --- Getters & Setters ---
    public int getBookId() { return bookId; }
    public void setBookId(int bookId) { this.bookId = bookId; }
    public int getBooktypeId() { return booktypeId; }
    public void setBooktypeId(int booktypeId) { this.booktypeId = booktypeId; }
    public String getBookname() { return bookname; }
    public void setBookname(String bookname) { this.bookname = bookname; }
    public String getAuthorname() { return authorname; }
    public void setAuthorname(String authorname) { this.authorname = authorname; }
    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }
    public boolean isAvailable() { return available; }
    public void setAvailable(boolean available) { this.available = available; }

    public Part getCoverPhoto() { return coverPhoto; }
    public void setCoverPhoto(Part coverPhoto) { this.coverPhoto = coverPhoto; }
    public Part getFrontPhoto() { return frontPhoto; }
    public void setFrontPhoto(Part frontPhoto) { this.frontPhoto = frontPhoto; }
    public Part getLastPhoto() { return lastPhoto; }
    public void setLastPhoto(Part lastPhoto) { this.lastPhoto = lastPhoto; }

    public String getCoverPhotoName() { return coverPhotoName; }
    public String getFrontPhotoName() { return frontPhotoName; }
    public String getLastPhotoName() { return lastPhotoName; }

    public List<Book> getAllBooks() { return bookSessionBean.getAllBooks(); }
    public List<Booktype> getAllBookTypes() { return bookSessionBean.getAllBookTypes(); }

    // --- Filter books by type ---
    public List<Book> getBooksByType() {
        if (booktypeId == 0 && !getAllBookTypes().isEmpty()) {
            booktypeId = getAllBookTypes().get(0).getId(); // default first category
        }
        return bookSessionBean.getBooksByType(booktypeId);
    }

    // --- Change selected type ---
    public void changeType(int typeId) {
        this.booktypeId = typeId;
    }
}
