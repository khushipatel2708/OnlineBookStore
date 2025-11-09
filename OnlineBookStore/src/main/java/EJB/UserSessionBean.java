package EJB;

import Entity.Book;
import Entity.Cart;
import Entity.FeedBack;
import Entity.GroupMaster;
import Entity.Payment;
import Entity.Shipping;
import Entity.User;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;

@Stateless
public class UserSessionBean implements UserSessionBeanLocal {

    @PersistenceContext(unitName = "my_pu")
    EntityManager em;

    private String encryptPassword(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hash = md.digest(password.getBytes());
            StringBuilder sb = new StringBuilder();
            for (byte b : hash) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean register(User user) {
        try {
            user.setPassword(encryptPassword(user.getPassword())); //hash password before saving
            em.persist(user);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public User login(String username, String password) {
        String encrypted = encryptPassword(password);
        List<User> list = em.createQuery("SELECT u FROM User u WHERE u.username = :username AND u.password = :password", User.class)
                .setParameter("username", username)
                .setParameter("password", encrypted)
                .getResultList();
        return list.isEmpty() ? null : list.get(0);
    }

    @Override
    public boolean isUsernameExists(String username) {
        return !em.createQuery("SELECT u FROM User u WHERE u.username = :username", User.class)
                .setParameter("username", username)
                .getResultList().isEmpty();
    }

    @Override
    public boolean isEmailExists(String email) {
        return !em.createQuery("SELECT u FROM User u WHERE u.email = :email", User.class)
                .setParameter("email", email)
                .getResultList().isEmpty();
    }

    @Override
    public boolean isPhoneExists(String phone) {
        return !em.createQuery("SELECT u FROM User u WHERE u.phone = :phone", User.class)
                .setParameter("phone", phone)
                .getResultList().isEmpty();
    }

    @Override
    public GroupMaster getGroupById(int groupId) {
        return em.find(GroupMaster.class, groupId);
    }

    @Override
    public boolean updateProfile(User user) {
        try {
            em.merge(user);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public User getUserById(int id) {
        return em.find(User.class, id);
    }

    @Override
    public boolean changePassword(String username, String oldPassword, String newPassword) {
        try {
            User user = em.createQuery("SELECT u FROM User u WHERE u.username = :username", User.class)
                    .setParameter("username", username)
                    .getSingleResult();
            if (user != null && user.getPassword().equals(encryptPassword(oldPassword))) {
                user.setPassword(encryptPassword(newPassword));
                em.merge(user);
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean resetPasswordByEmail(String email, String newPassword) {
        try {
            User user = em.createQuery("SELECT u FROM User u WHERE u.email = :email", User.class)
                    .setParameter("email", email)
                    .getSingleResult();

            if (user != null) {
                user.setPassword(encryptPassword(newPassword));
                em.merge(user);
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public List<User> getAllUsers() {
        return em.createQuery("SELECT u FROM User u", User.class).getResultList();
    }

    //shipping logic
    @Override
    public void addShipping(Shipping s) {
        em.persist(s);
    }

    @Override
    public void updateShipping(Shipping s) {
        em.merge(s);
    }

    @Override
    public void deleteShipping(int id) {
        Shipping s = em.find(Shipping.class, id);
        if (s != null) {
            em.remove(s);
        }
    }

    @Override
    public Shipping getShippingById(int id) {
        return em.find(Shipping.class, id);
    }

    @Override
    public List<Shipping> getShippingByUser(int userid) {
        return em.createQuery("SELECT s FROM Shipping s WHERE s.userid = :uid", Shipping.class)
                .setParameter("uid", userid)
                .getResultList();
    }

    //Cart Logic
    @Override
    public void addToCart(int userId, int bookId) {
        User user = em.find(User.class, userId);
        Book book = em.find(Book.class, bookId);

        if (user != null && book != null) {
            // Check if this book is already in the user's cart
            List<Cart> existing = em.createQuery(
                    "SELECT c FROM Cart c WHERE c.user.id = :uid AND c.book.id = :bid", Cart.class)
                    .setParameter("uid", userId)
                    .setParameter("bid", bookId)
                    .getResultList();

            if (!existing.isEmpty()) {
                // Increase quantity if already exists
                Cart cart = existing.get(0);
                cart.setQuantity(cart.getQuantity() + 1);
                em.merge(cart);
            } else {
                // Otherwise, create new cart entry
                Cart cart = new Cart();
                cart.setUser(user);
                cart.setBook(book);
                cart.setQuantity(1);
                em.persist(cart);
            }
        }
    }

    @Override
    public List<Cart> getCartItems(int userId) {
        return em.createQuery("SELECT c FROM Cart c WHERE c.user.id = :uid", Cart.class)
                .setParameter("uid", userId)
                .getResultList();
    }

    @Override
    public void removeFromCart(int cartId) {
        Cart cart = em.find(Cart.class, cartId);
        if (cart != null) {
            em.remove(cart);
        }
    }

    public void updateQuantity(int cartId, int change) {
        Cart cart = em.find(Cart.class, cartId);
        if (cart != null) {
            int newQty = cart.getQuantity() + change;
            if (newQty <= 0) {
                em.remove(cart); // remove if quantity becomes 0
            } else {
                cart.setQuantity(newQty);
                em.merge(cart);
            }
        }
    }

    //Payment Logic
    @Override
    public void addPayment(Payment payment) {
        em.persist(payment);
    }

    @Override
    public void updatePayment(Payment payment) {
        em.merge(payment);
    }

    @Override
    public Payment findById(int id) {
        return em.find(Payment.class, id);
    }

    //FeedBack Logic
    @Override
    public void addFeedback(FeedBack f) {
        em.persist(f);
    }

    @Override
    public void updateFeedback(FeedBack f) {
        em.merge(f);
    }

    @Override
    public void deleteFeedback(int id) {
        FeedBack f = em.find(FeedBack.class, id);
        if (f != null) {
            em.remove(f);
        }
    }

    @Override
    public FeedBack getFeedbackById(int id) {
        return em.find(FeedBack.class, id);
    }

    @Override
    public List<FeedBack> getAllFeedback() {
        return em.createQuery("SELECT f FROM FeedBack f", FeedBack.class).getResultList();
    }

    @Override
    public List<FeedBack> getFeedbackByBook(int bookId) {
        return em.createQuery("SELECT f FROM FeedBack f WHERE f.book.id = :bid", FeedBack.class)
                .setParameter("bid", bookId)
                .getResultList();
    }

    @Override
    public List<FeedBack> getFeedbackByUser(int userId) {
        return em.createQuery("SELECT f FROM FeedBack f WHERE f.user.id = :uid", FeedBack.class)
                .setParameter("uid", userId)
                .getResultList();
    }
}
