/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Entity;

import jakarta.json.bind.annotation.JsonbTransient;
import jakarta.persistence.Basic;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.NamedQueries;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlTransient;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Collection;

/**
 *
 * @author KHUSHI PC
 */
@Entity
@Table(name = "book")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Book.findAll", query = "SELECT b FROM Book b"),
    @NamedQuery(name = "Book.findById", query = "SELECT b FROM Book b WHERE b.id = :id"),
    @NamedQuery(name = "Book.findByBookname", query = "SELECT b FROM Book b WHERE b.bookname = :bookname"),
    @NamedQuery(name = "Book.findByAuthorname", query = "SELECT b FROM Book b WHERE b.authorname = :authorname"),
    @NamedQuery(name = "Book.findByPrice", query = "SELECT b FROM Book b WHERE b.price = :price"),
    @NamedQuery(name = "Book.findByAvailable", query = "SELECT b FROM Book b WHERE b.available = :available"),
    @NamedQuery(name = "Book.findByCoverPhoto", query = "SELECT b FROM Book b WHERE b.coverPhoto = :coverPhoto"),
    @NamedQuery(name = "Book.findByFrontPagePhoto", query = "SELECT b FROM Book b WHERE b.frontPagePhoto = :frontPagePhoto"),
    @NamedQuery(name = "Book.findByLastPagePhoto", query = "SELECT b FROM Book b WHERE b.lastPagePhoto = :lastPagePhoto")})
public class Book implements Serializable {

    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "bookname")
    private String bookname;
    @Basic(optional = false)
    @NotNull()
    @Size(min = 1, max = 255)
    @Column(name = "authorname")
    private String authorname;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Basic(optional = false)
    @NotNull
    @Column(name = "price")
    private BigDecimal price;
    @Basic(optional = false)
    @NotNull
    @Column(name = "available")
    private int available;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Size(max = 255)
    @Column(name = "cover_photo")
    private String coverPhoto;
    @Size(max = 255)
    @Column(name = "front_page_photo")
    private String frontPagePhoto;
    @Size(max = 255)
    @Column(name = "last_page_photo")
    private String lastPagePhoto;
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @OneToMany(mappedBy = "bookId")
    private Collection<Feedback> feedbackCollection;
    @JoinColumn(name = "booktype_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Booktype booktypeId;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "bookId")
    private Collection<Cart> cartCollection;

    public Book() {
    }

    public Book(Integer id) {
        this.id = id;
    }

    public Book(Integer id, String bookname, String authorname, BigDecimal price) {
        this.id = id;
        this.bookname = bookname;
        this.authorname = authorname;
        this.price = price;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }


    public String getCoverPhoto() {
        return coverPhoto;
    }

    public void setCoverPhoto(String coverPhoto) {
        this.coverPhoto = coverPhoto;
    }

    public String getFrontPagePhoto() {
        return frontPagePhoto;
    }

    public void setFrontPagePhoto(String frontPagePhoto) {
        this.frontPagePhoto = frontPagePhoto;
    }

    public String getLastPagePhoto() {
        return lastPagePhoto;
    }

    public void setLastPagePhoto(String lastPagePhoto) {
        this.lastPagePhoto = lastPagePhoto;
    }

    @XmlTransient
    public Collection<Feedback> getFeedbackCollection() {
        return feedbackCollection;
    }

    public void setFeedbackCollection(Collection<Feedback> feedbackCollection) {
        this.feedbackCollection = feedbackCollection;
    }

    public Booktype getBooktypeId() {
        return booktypeId;
    }

    public void setBooktypeId(Booktype booktypeId) {
        this.booktypeId = booktypeId;
    }

    @XmlTransient
    @JsonbTransient
    public Collection<Cart> getCartCollection() {
        return cartCollection;
    }

    public void setCartCollection(Collection<Cart> cartCollection) {
        this.cartCollection = cartCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Book)) {
            return false;
        }
        Book other = (Book) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Entity.Book[ id=" + id + " ]";
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

    public int getAvailable() {
        return available;
    }

    public void setAvailable(int available) {
        this.available = available;
    }
    
}
