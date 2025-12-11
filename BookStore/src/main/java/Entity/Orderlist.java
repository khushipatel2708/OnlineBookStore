/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Entity;

import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.NamedQueries;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.validation.constraints.Size;
import jakarta.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.math.BigInteger;
import java.util.Date;

/**
 *
 * @author 91931
 */
@Entity
@Table(name = "orderlist")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Orderlist.findAll", query = "SELECT o FROM Orderlist o"),
    @NamedQuery(name = "Orderlist.findByOrderId", query = "SELECT o FROM Orderlist o WHERE o.orderId = :orderId"),
    @NamedQuery(name = "Orderlist.findByOrderDate", query = "SELECT o FROM Orderlist o WHERE o.orderDate = :orderDate"),
    @NamedQuery(name = "Orderlist.findByOrderTime", query = "SELECT o FROM Orderlist o WHERE o.orderTime = :orderTime"),
    @NamedQuery(name = "Orderlist.findByStatus", query = "SELECT o FROM Orderlist o WHERE o.status = :status"),
    @NamedQuery(name = "Orderlist.findByTotalPrice", query = "SELECT o FROM Orderlist o WHERE o.totalPrice = :totalPrice")})
public class Orderlist implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "orderId")
    private Integer orderId;
    @Column(name = "orderDate")
    @Temporal(TemporalType.DATE)
    private Date orderDate;
    @Column(name = "orderTime")
    @Temporal(TemporalType.TIME)
    private Date orderTime;
    @Size(max = 255)
    @Column(name = "status")
    private String status;
    @Column(name = "totalPrice")
    private BigInteger totalPrice;
    @JoinColumn(name = "bookId", referencedColumnName = "id")
    @ManyToOne
    private Book bookId;
    @JoinColumn(name = "userId", referencedColumnName = "Id")
    @ManyToOne
    private User userId;

    public Orderlist() {
    }

    public Orderlist(Integer orderId) {
        this.orderId = orderId;
    }

    public Integer getOrderId() {
        return orderId;
    }

    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
    }

    public Date getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(Date orderDate) {
        this.orderDate = orderDate;
    }

    public Date getOrderTime() {
        return orderTime;
    }

    public void setOrderTime(Date orderTime) {
        this.orderTime = orderTime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public BigInteger getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(BigInteger totalPrice) {
        this.totalPrice = totalPrice;
    }

    public Book getBookId() {
        return bookId;
    }

    public void setBookId(Book bookId) {
        this.bookId = bookId;
    }

    public User getUserId() {
        return userId;
    }

    public void setUserId(User userId) {
        this.userId = userId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (orderId != null ? orderId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Orderlist)) {
            return false;
        }
        Orderlist other = (Orderlist) object;
        if ((this.orderId == null && other.orderId != null) || (this.orderId != null && !this.orderId.equals(other.orderId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Entity.Orderlist[ orderId=" + orderId + " ]";
    }
    
}
