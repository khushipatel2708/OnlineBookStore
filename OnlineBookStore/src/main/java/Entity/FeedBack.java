package Entity;

import jakarta.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "feedback")
public class FeedBack implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    // ManyToOne relations to Book and User (no cascading here)
    @ManyToOne
    @JoinColumn(name = "bookId")
    private Book book;

    @ManyToOne
    @JoinColumn(name = "userId")
    private User user;

    private Integer rating;

    @Column(length = 255)
    private String comments;

    // getters / setters
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public Book getBook() { return book; }
    public void setBook(Book book) { this.book = book; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }

    public Integer getRating() { return rating; }
    public void setRating(Integer rating) { this.rating = rating; }

    public String getComments() { return comments; }
    public void setComments(String comments) { this.comments = comments; }
}
