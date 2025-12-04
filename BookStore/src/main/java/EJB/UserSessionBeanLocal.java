/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB40/SessionLocal.java to edit this template
 */
package EJB;

import Entity.Cart;
import Entity.Shipping;
import jakarta.ejb.Local;
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
    
    
   //cart
    void addToCart(int userId, int bookId);
    List<Cart> getCartItems(int userId);
    void removeFromCart(int cartId);
    void updateQuantity(int cartId, int change);
    
    
    

}
