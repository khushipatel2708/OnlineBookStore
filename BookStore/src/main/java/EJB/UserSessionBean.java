/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB40/StatelessEjbClass.java to edit this template
 */
package EJB;

import Entity.Book;
import Entity.Cart;
import Entity.City;
import Entity.Orderlist;
import Entity.Payment;
import Entity.Shipping;
import Entity.User;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import java.math.BigDecimal;
import java.util.Collection;
import java.util.List;

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
    public void addToCart(int userId, int bookId) {
        User user = em.find(User.class, userId);
        Book book = em.find(Book.class, bookId);

        if (user != null && book != null) {

            // FIXED QUERY ---------------------- vvvvvvvvvvvvvvvvvvvvv
            List<Cart> existing = em.createQuery(
                    "SELECT c FROM Cart c WHERE c.userId.id = :uid AND c.bookId.id = :bid", Cart.class)
                    .setParameter("uid", userId)
                    .setParameter("bid", bookId)
                    .getResultList();
            // FIXED QUERY ---------------------- ^^^^^^^^^^^^^^^^^^^^^

            if (!existing.isEmpty()) {
                Cart cart = existing.get(0);
                cart.setQuantity(cart.getQuantity() + 1);
                em.merge(cart);
            } else {
                Cart cart = new Cart();
                cart.setUserId(user);
                cart.setBookId(book);
                cart.setQuantity(1);
                em.persist(cart);
            }
        }
    }

    @Override
    public List<Cart> getCartItems(int userId) {

        // FIXED QUERY ---------------------- vvvvvvvvvvvvvvvvvvvvv
        return em.createQuery(
                "SELECT c FROM Cart c WHERE c.userId.id = :uid", Cart.class)
                .setParameter("uid", userId)
                .getResultList();
        // FIXED QUERY ---------------------- ^^^^^^^^^^^^^^^^^^^^^
    }

    @Override
    public void removeFromCart(int cartId) {
        Cart cart = em.find(Cart.class, cartId);
        if (cart != null) {
            em.remove(cart);
        }
    }

    @Override
    public void updateQuantity(int cartId, int change) {
        Cart cart = em.find(Cart.class, cartId);
        if (cart != null) {
            int newQty = cart.getQuantity() + change;
            if (newQty <= 0) {
                em.remove(cart);
            } else {
                cart.setQuantity(newQty);
                em.merge(cart);
            }
        }
    }

    @Override
    public void addPayment(User user, Book book, String paymentMethod, BigDecimal amount, String phone, String status) {
        Payment payment = new Payment();
        payment.setUserId(user);
        payment.setBookId(book);
        payment.setPaymentMethod(paymentMethod);
        payment.setAmount(amount);
        payment.setPhone(phone);
        payment.setStatus(status);
        em.persist(payment);
        // Automatically create order after payment
        createOrderFromPayment(payment);
    }

    // Get all pending orders for admin (status != Delivered)
    @Override
    public List<Orderlist> getPendingOrders() {
        TypedQuery<Orderlist> query = em.createQuery(
                "SELECT o FROM Orderlist o WHERE o.status <> :delivered ORDER BY o.orderDate DESC", Orderlist.class);
        query.setParameter("delivered", "Delivered");
        return query.getResultList();
    }

    // Mark order as delivered
    @Override
    public void markAsDelivered(Integer orderId) {
        Orderlist order = em.find(Orderlist.class, orderId);
        if (order != null) {
            order.setStatus("Delivered");
            em.merge(order);
        }
    }

    // Get orders for a specific user
    @Override
    public List<Orderlist> getOrdersByUser(User user) {
        TypedQuery<Orderlist> query = em.createQuery(
                "SELECT o FROM Orderlist o WHERE o.userId = :user ORDER BY o.orderDate DESC", Orderlist.class);
        query.setParameter("user", user);
        return query.getResultList();
    }

    // Create order from payment
    @Override
    public void createOrderFromPayment(Payment payment) {
        Orderlist order = new Orderlist();
        order.setUserId(payment.getUserId());
        order.setOrderDate(new java.util.Date());
        order.setOrderTime(new java.util.Date());
        order.setTotalPrice(payment.getAmount());
        order.setStatus("Pending");
        em.persist(order);
    }

    @Override
    public void addCODPayment(Integer userId, Integer bookId, BigDecimal amount) {
        User user = em.find(User.class, userId);
        Book book = em.find(Book.class, bookId);

        Payment payment = new Payment();
        payment.setUserId(user);
        payment.setBookId(book);
        payment.setAmount(amount);
        payment.setPaymentMethod("COD");
        payment.setPhone(user.getPhone()); // <-- Fetch user phone dynamically
        payment.setStatus("Paid"); // COD = Paid

        em.persist(payment);
    }

    @Override
    public void updatePaymentStatus(Integer userId, String oldStatus, String newStatus) {
        TypedQuery<Payment> q = em.createQuery(
                "SELECT p FROM Payment p WHERE p.userId.id = :uid AND p.status = :old",
                Payment.class
        );

        q.setParameter("uid", userId);
        q.setParameter("old", oldStatus);

        List<Payment> list = q.getResultList();

        for (Payment p : list) {
            p.setStatus(newStatus);
            em.merge(p);
        }
    }

}
