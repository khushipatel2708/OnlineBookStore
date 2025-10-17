package EJB;

import Entity.GroupMaster;
import Entity.User;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.List;

@Stateless
    public class UserSessionBean implements UserSessionBeanLocal {

   @PersistenceContext(unitName = "my_pu")
    EntityManager em;

    @Override
    public boolean register(User user) {
        try {
            em.persist(user);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public User login(String username, String password) {
        List<User> list = em.createQuery("SELECT u FROM User u WHERE u.username = :username AND u.password = :password", User.class)
                .setParameter("username", username)
                .setParameter("password", password)
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

    // --- FETCH GROUP ---
    @Override
    public GroupMaster getGroupById(int groupId) {
        return em.find(GroupMaster.class, groupId);
    }
}
