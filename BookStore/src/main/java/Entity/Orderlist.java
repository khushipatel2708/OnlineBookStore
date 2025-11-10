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
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import jakarta.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 *
 * @author KHUSHI PC
 */
@Entity
@Table(name = "orderlist")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Orderlist.findAll", query = "SELECT o FROM Orderlist o"),
    @NamedQuery(name = "Orderlist.findByOrderId", query = "SELECT o FROM Orderlist o WHERE o.orderId = :orderId"),
    @NamedQuery(name = "Orderlist.findByOrderDate", query = "SELECT o FROM Orderlist o WHERE o.orderDate = :orderDate"),
    @NamedQuery(name = "Orderlist.findByOrderTime", query = "SELECT o FROM Orderlist o WHERE o.orderTime = :orderTime"),
    @NamedQuery(name = "Orderlist.findByTotalPrice", query = "SELECT o FROM Orderlist o WHERE o.totalPrice = :totalPrice"),
    @NamedQuery(name = "Orderlist.findByStatus", query = "SELECT o FROM Orderlist o WHERE o.status = :status")})
public class Orderlist implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "orderId")
    private Integer orderId;
    @Basic(optional = false)
    @NotNull
    @Column(name = "orderDate")
    @Temporal(TemporalType.DATE)
    private Date orderDate;
    @Basic(optional = false)
    @NotNull
    @Column(name = "orderTime")
    @Temporal(TemporalType.TIME)
    private Date orderTime;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Basic(optional = false)
    @NotNull
    @Column(name = "totalPrice")
    private BigDecimal totalPrice;
    @Size(max = 50)
    @Column(name = "status")
    private String status;
    @JoinColumn(name = "userId", referencedColumnName = "Id")
    @ManyToOne(optional = false)
    private User userId;

    public Orderlist() {
    }

    public Orderlist(Integer orderId) {
        this.orderId = orderId;
    }

    public Orderlist(Integer orderId, Date orderDate, Date orderTime, BigDecimal totalPrice) {
        this.orderId = orderId;
        this.orderDate = orderDate;
        this.orderTime = orderTime;
        this.totalPrice = totalPrice;
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

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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
