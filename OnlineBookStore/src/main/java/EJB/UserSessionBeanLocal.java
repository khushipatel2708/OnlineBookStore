package EJB;

import Entity.User;
import jakarta.ejb.Local;

@Local
public interface UserSessionBeanLocal {
      boolean register(User user);
    User login(String username, String password);
    boolean isUsernameExists(String username);
boolean isEmailExists(String email);
boolean isPhoneExists(String phone);


}
