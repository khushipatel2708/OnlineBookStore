package Entity;

import jakarta.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "book")
public class Book implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "booktype_id")
    private Booktype booktype;

    private String bookname;
    private String authorname;
    private double price;
    private boolean available;

    private String cover_photo;
    private String front_page_photo;
    private String last_page_photo;

    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public Booktype getBooktype() { return booktype; }
    public void setBooktype(Booktype booktype) { this.booktype = booktype; }

    public String getBookname() { return bookname; }
    public void setBookname(String bookname) { this.bookname = bookname; }

    public String getAuthorname() { return authorname; }
    public void setAuthorname(String authorname) { this.authorname = authorname; }

    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }

    public boolean isAvailable() { return available; }
    public void setAvailable(boolean available) { this.available = available; }

    public String getCover_photo() { return cover_photo; }
    public void setCover_photo(String cover_photo) { this.cover_photo = cover_photo; }

    public String getFront_page_photo() { return front_page_photo; }
    public void setFront_page_photo(String front_page_photo) { this.front_page_photo = front_page_photo; }

    public String getLast_page_photo() { return last_page_photo; }
    public void setLast_page_photo(String last_page_photo) { this.last_page_photo = last_page_photo; }
}
