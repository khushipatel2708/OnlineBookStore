package EJB;

import Entity.GroupMaster;
import Entity.User;
import jakarta.ejb.Local;

@Local
public interface UserSessionBeanLocal {
      boolean register(User user);
    User login(String username, String password);
    boolean isUsernameExists(String username);
boolean isEmailExists(String email);
boolean isPhoneExists(String phone);
 // NEW METHOD
    GroupMaster getGroupById(int groupId);
    
    boolean updateProfile(User user);
    User getUserById(int id);
      boolean changePassword(String username, String oldPassword, String newPassword);
        boolean resetPasswordByEmail(String email, String newPassword);
}
