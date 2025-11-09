package EJB;

import Entity.Cart;
import Entity.FeedBack;
import Entity.GroupMaster;
import Entity.Payment;
import Entity.Shipping;
import Entity.User;
import jakarta.ejb.Local;
import java.util.List;

@Local
public interface UserSessionBeanLocal {

    //User
    boolean register(User user);
    User login(String username, String password);
    boolean isUsernameExists(String username);
    boolean isEmailExists(String email);
    boolean isPhoneExists(String phone);
    GroupMaster getGroupById(int groupId);
    boolean updateProfile(User user);
    User getUserById(int id);
    boolean changePassword(String username, String oldPassword, String newPassword);
    boolean resetPasswordByEmail(String email, String newPassword);
    List<User> getAllUsers();
    
    //Shipping
    void addShipping(Shipping s);
    void updateShipping(Shipping s);
    void deleteShipping(int id);
    Shipping getShippingById(int id);
    List<Shipping> getShippingByUser(int userid);
    
    //cart
    void addToCart(int userId, int bookId);
    List<Cart> getCartItems(int userId);
    void removeFromCart(int cartId);
    void updateQuantity(int cartId, int change);
    
    //Payment
    void addPayment(Payment payment);
    void updatePayment(Payment payment);
    Payment findById(int id);
    
   //FeedBack 
    void addFeedback(FeedBack f);
    void updateFeedback(FeedBack f);
    void deleteFeedback(int id);
    FeedBack getFeedbackById(int id);
    List<FeedBack> getAllFeedback();
    List<FeedBack> getFeedbackByBook(int bookId);
    List<FeedBack> getFeedbackByUser(int userId);
}
