/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB40/StatelessEjbClass.java to edit this template
 */
package EJB;

import Entity.Book;
import Entity.Cart;
import Entity.City;
import Entity.Shipping;
import Entity.User;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.Collection;

/**
 *
 * @author KHUSHI PC
 */
@Stateless
public class UserSessionBean implements UserSessionBeanLocal {

    @PersistenceContext(unitName = "my_pu")
    EntityManager em;

    // ✅ Get all shippings
    @Override
    public Collection<Shipping> getAllShippings() {
        return em.createNamedQuery("Shipping.findAll").getResultList();
    }

    // ✅ Get single shipping by id
    @Override
    public Shipping getShippingById(Integer id) {
        return em.find(Shipping.class, id);
    }

    // ✅ Add new shipping
    @Override
    public void addShipping(Integer userId, Integer cityId, String name, String phone, String address1, String address2, String landmark, String pincode) {
        User user = em.find(User.class, userId);
        City city = cityId != null ? em.find(City.class, cityId) : null;

        Shipping s = new Shipping();
        s.setUserid(user);
        s.setCityid(city);
        s.setName(name);
        s.setPhone(phone);
        s.setAddress1(address1);
        s.setAddress2(address2);
        s.setLandmark(landmark);
        s.setPincode(pincode);

        em.persist(s);
    }

    // ✅ Update existing shipping
    @Override
    public void updateShipping(Integer id, Integer userId, Integer cityId, String name, String phone, String address1, String address2, String landmark, String pincode) {
        Shipping s = em.find(Shipping.class, id);
        if (s != null) {
            User user = em.find(User.class, userId);
            City city = cityId != null ? em.find(City.class, cityId) : null;

            s.setUserid(user);
            s.setCityid(city);
            s.setName(name);
            s.setPhone(phone);
            s.setAddress1(address1);
            s.setAddress2(address2);
            s.setLandmark(landmark);
            s.setPincode(pincode);

            em.merge(s);
        }
    }

    // ✅ Get shippings by City
    @Override
    public Collection<Shipping> getShippingByCity(Integer cityId) {
        return em.createQuery("SELECT s FROM Shipping s WHERE s.cityid.id = :cityId", Shipping.class)
                .setParameter("cityId", cityId)
                .getResultList();
    }

    // ✅ Get shippings by Pincode
    @Override
    public Collection<Shipping> getShippingByPincode(String pincode) {
        return em.createQuery("SELECT s FROM Shipping s WHERE s.pincode = :pincode", Shipping.class)
                .setParameter("pincode", pincode)
                .getResultList();
    }

    // ✅ Get shippings by User
    @Override
    public Collection<Shipping> getShippingByUser(Integer userId) {
        return em.createQuery("SELECT s FROM Shipping s WHERE s.userid.id = :userId", Shipping.class)
                .setParameter("userId", userId)
                .getResultList();
    }
    
     // ✅ Delete shipping
    @Override
    public void removeShipping(Integer id) {
        Shipping s = em.find(Shipping.class, id);
        if (s != null) {
            em.remove(s);
        }
    }
    
    
    // ================= CART OPERATIONS ==================

    @Override
    public void addToCart(Integer userId, Integer bookId, Integer quantity) {
        User user = em.find(User.class, userId);
        Book book = em.find(Book.class, bookId);

        if (user != null && book != null) {
            // Check if the item already exists in the cart
            Cart existingCart = null;
            try {
                existingCart = em.createQuery(
                        "SELECT c FROM Cart c WHERE c.userId.id = :userId AND c.bookId.id = :bookId", Cart.class)
                        .setParameter("userId", userId)
                        .setParameter("bookId", bookId)
                        .getSingleResult();
            } catch (Exception e) {
                // no existing item found
            }

            if (existingCart != null) {
                // update quantity
                existingCart.setQuantity(existingCart.getQuantity() + quantity);
                em.merge(existingCart);
            } else {
                // create new cart entry
                Cart c = new Cart();
                c.setUserId(user);
                c.setBookId(book);
                c.setQuantity(quantity);
                em.persist(c);
            }
        }
    }

    @Override
    public Collection<Cart> getCartByUser(Integer userId) {
        return em.createQuery("SELECT c FROM Cart c WHERE c.userId.id = :userId", Cart.class)
                .setParameter("userId", userId)
                .getResultList();
    }

    @Override
    public void updateCartQuantity(Integer cartId, Integer quantity) {
        Cart c = em.find(Cart.class, cartId);
        if (c != null && quantity > 0) {
            c.setQuantity(quantity);
            em.merge(c);
        }
    }

    @Override
    public void deleteCartItem(Integer cartId) {
        Cart c = em.find(Cart.class, cartId);
        if (c != null) {
            em.remove(c);
        }
    }

}