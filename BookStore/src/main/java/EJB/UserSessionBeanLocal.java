/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB40/SessionLocal.java to edit this template
 */
package EJB;

import Entity.Book;
import Entity.Cart;
import Entity.Feedback;
import Entity.Orderlist;
import Entity.Payment;
import Entity.Shipping;
import Entity.User;
import jakarta.ejb.Local;
import java.math.BigDecimal;
import java.util.Collection;
import java.util.List;

/**
 *
 * @author KHUSHI PC
 */
@Local
public interface UserSessionBeanLocal {
    
     // --- Shipping CRUD methods ---
    Collection<Shipping> getAllShippings();
    Shipping getShippingById(Integer id);
    void addShipping(Integer userId, Integer cityId, String name, String phone, String address1, String address2, String landmark, String pincode);
    void updateShipping(Integer id, Integer userId, Integer cityId, String name, String phone, String address1, String address2, String landmark, String pincode);
    void removeShipping(Integer id);    
    // --- shipping Filters ---
    Collection<Shipping> getShippingByCity(Integer cityId);
    Collection<Shipping> getShippingByPincode(String pincode);
    Collection<Shipping> getShippingByUser(Integer userId);
    Shipping getLatestShippingByUser(Integer userId);
    Cart getCartById(int cartId);
void updateBookStock(Integer bookId, int newAvailable);
    
   //cart
    void addToCart(int userId, int bookId);
    List<Cart> getCartItems(int userId);
    void removeFromCart(int cartId);
    void updateQuantity(int cartId, int change);
    
      void addPayment(User user, Book book, String paymentMethod, BigDecimal amount, String phone, String status);
    
    List<Orderlist> getPendingOrders();
    void markAsDelivered(Integer orderId);
    List<Orderlist> getOrdersByUser(User user);
    void createOrderFromPayment(Payment payment);
    public void addCODPayment(Integer userId, Integer bookId, BigDecimal amount);
    Orderlist getOrderById(Integer orderId);
    public void updatePaymentStatus(Integer userId, String oldStatus, String newStatus);
    public void addFeedback(String comments, int rating, int bookId, int userId);
List<Feedback> getAllFeedbacks();
List<Payment> getAllPayments();

    
}
