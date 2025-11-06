package EJB;

import Entity.GroupMaster;
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
            for (byte b : hash) sb.append(String.format("%02x", b));
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
}
