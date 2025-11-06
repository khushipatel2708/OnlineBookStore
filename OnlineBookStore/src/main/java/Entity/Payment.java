package Entity;

import jakarta.persistence.*;

@Entity
@Table(name = "payment")
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private int bookId;
    private int userId;
    private String payment_method;
    private String phone;

    @Enumerated(EnumType.STRING)
    private Status status = Status.Pending;

    public enum Status {
        Pending, Completed, Failed
    }

    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getBookId() { return bookId; }
    public void setBookId(int bookId) { this.bookId = bookId; }

    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }

    public String getPayment_method() { return payment_method; }
    public void setPayment_method(String payment_method) { this.payment_method = payment_method; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public Status getStatus() { return status; }
    public void setStatus(Status status) { this.status = status; }
}
