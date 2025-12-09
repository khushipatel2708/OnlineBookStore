/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB40/StatelessEjbClass.java to edit this template
 */
package EJB;

import Entity.Book;
import Entity.Booktype;
import Entity.City;
import Entity.GroupMaster;
import Entity.Shipping;
import Entity.User;
import jakarta.annotation.security.DeclareRoles;
import jakarta.annotation.security.PermitAll;
import jakarta.annotation.security.RolesAllowed;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.math.BigDecimal;
import java.util.Collection;

@Stateless
@DeclareRoles({"Admin", "User"})
public class AdminSessionBean implements AdminSessionBeanLocal {

    @PersistenceContext(unitName = "my_pu")
    EntityManager em;

    //===============User=====================
    @PermitAll
    @Override
    public Collection<User> getAllUsers() {
        return em.createNamedQuery("User.findAll", User.class).getResultList();
    }

    @PermitAll
    @Override
    public User findUserById(Integer id) {
        return em.find(User.class, id);
    }

    @PermitAll
    @Override
    public void addUser(String fullname, String phone, String username, String email, String password, String status, Integer groupId) {
        GroupMaster group = em.find(GroupMaster.class, groupId);
        if (group != null) {
            User u = new User();
            u.setFullname(fullname);
            u.setPhone(phone);
            u.setUsername(username);
            u.setEmail(email);
            u.setPassword(hashPassword(password)); // hash the password only ONCE
            u.setStatus(status != null ? status : "Active");
            u.setGroupid(group);
            em.persist(u);
        }
    }

    @PermitAll
    @Override
    public void updateUser(Integer id, String fullname, String phone, String username, String email, String status, Integer groupId) {
        User u = em.find(User.class, id);
        if (u != null) {
            GroupMaster group = em.find(GroupMaster.class, groupId);
            u.setFullname(fullname);
            u.setPhone(phone);
            u.setUsername(username);
            u.setEmail(email);
            u.setStatus(status);
            u.setGroupid(group);
            em.merge(u);
        }
    }

    @PermitAll
    @Override
    public void deleteUser(Integer id) {
        User u = em.find(User.class, id);
        if (u != null) {
            em.remove(u);
        }
    }

    @PermitAll
    @Override
    public User findUserByUsername(String username) {
        try {
            return em.createQuery("SELECT u FROM User u WHERE u.username = :username", User.class)
                    .setParameter("username", username)
                    .getSingleResult();
        } catch (Exception e) {
            return null;
        }
    }

    // ---------- BOOKTYPE, BOOK, CITY, GROUP : all @PermitAll ----------
    @PermitAll
    @Override
    public Collection<Booktype> getAllBooktypes() {
        return em.createNamedQuery("Booktype.findAll").getResultList();
    }

    @PermitAll
    @Override
    public void addBooktype(String type, String description) {
        Booktype bt = new Booktype();
        bt.setType(type);
        bt.setDescription(description);
        em.persist(bt);
    }

    @PermitAll
    @Override
    public void updateBooktype(Integer id, String type, String description) {
        Booktype bt = em.find(Booktype.class, id);
        if (bt != null) {
            bt.setType(type);
            bt.setDescription(description);
            em.merge(bt);
        }
    }

    @PermitAll
    @Override
    public void deleteBooktype(Integer id) {
        Booktype bt = em.find(Booktype.class, id);
        if (bt != null) {
            em.remove(bt);
        }
    }

    @PermitAll
    @Override
    public Collection<Book> getAllBooks() {
        return em.createNamedQuery("Book.findAll").getResultList();
    }

    @PermitAll
    @Override
    public void addBook(String bookname, String authorname, Double price, Integer booktypeId,Integer available,
            String coverPhoto, String frontPagePhoto, String lastPagePhoto) {

        Booktype bt = em.find(Booktype.class, booktypeId);
        if (bt != null) {
            Book b = new Book();
            b.setBookname(bookname);
            b.setAuthorname(authorname);
            b.setPrice(BigDecimal.valueOf(price));
            b.setBooktypeId(bt);
            b.setAvailable(available);
            b.setCoverPhoto(coverPhoto);
            b.setFrontPagePhoto(frontPagePhoto);
            b.setLastPagePhoto(lastPagePhoto);
            em.persist(b);
        }
    }

    @PermitAll
    @Override
    public void updateBook(Integer id, String bookname, String authorname, Double price, Integer booktypeId,Integer available,
            String coverPhoto, String frontPagePhoto, String lastPagePhoto) {

        Book b = em.find(Book.class, id);
        if (b != null) {
            Booktype bt = em.find(Booktype.class, booktypeId);
            b.setBookname(bookname);
            b.setAuthorname(authorname);
            b.setPrice(BigDecimal.valueOf(price));
            b.setBooktypeId(bt);
            b.setAvailable(available);
            b.setCoverPhoto(coverPhoto);
            b.setFrontPagePhoto(frontPagePhoto);
            b.setLastPagePhoto(lastPagePhoto);
            em.merge(b);
        }
    }

    @PermitAll
    @Override
    public void deleteBook(Integer id) {
        Book b = em.find(Book.class, id);
        if (b != null) {
            em.remove(b);
        }
    }

    @PermitAll
    @Override
    public Collection<City> getAllCities() {
        return em.createNamedQuery("City.findAll").getResultList();
    }

    @PermitAll
    @Override
    public void addCity(String name) {
        City city = new City();
        city.setName(name);
        em.persist(city);
    }

    @PermitAll
    @Override
    public void updateCity(Integer id, String name) {
        City city = em.find(City.class, id);
        if (city != null) {
            city.setName(name);
            em.merge(city);
        }
    }

    @PermitAll
    @Override
    public void removeCity(Integer id) {
        City city = em.find(City.class, id);
        if (city != null) {
            em.remove(city);
        }
    }

    @PermitAll
    @Override
    public City findCityById(Integer id) {
        return em.find(City.class, id);
    }

    @PermitAll
    @Override
    public Collection<City> findCityByName(String name) {
        return em.createQuery("SELECT c FROM City c WHERE c.name = :name", City.class)
                .setParameter("name", name)
                .getResultList();
    }

    @PermitAll
    @Override
    public void addGroup(String groupname, String username) {
        GroupMaster g = new GroupMaster();
        g.setGroupname(groupname);
        g.setUsername(username);
        em.persist(g);
    }

    @PermitAll
    @Override
    public void updateGroup(Integer id, String groupname, String username) {
        GroupMaster g = em.find(GroupMaster.class, id);
        if (g != null) {
            g.setGroupname(groupname);
            g.setUsername(username);
            em.merge(g);
        }
    }

    @PermitAll
    @Override
    public void removeGroup(Integer id) {
        GroupMaster g = em.find(GroupMaster.class, id);
        if (g != null) {
            em.remove(g);
        }
    }

    @PermitAll
    @Override
    public Collection<GroupMaster> getAllGroups() {
        return em.createNamedQuery("GroupMaster.findAll").getResultList();
    }

    @Override
    public Booktype getBooktypeById(Integer id) {
        return em.find(Booktype.class, id);
//        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

//    @Override
//    public Book findBookById(Integer id) {
//        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
//    }
    @Override
    public Book findBookById(Integer id) {
        return em.find(Book.class, id);
    }

    @Override
    public GroupMaster findGroupById(Integer id) {
        return em.find(GroupMaster.class, id);
    }

    @Override
    public Collection<GroupMaster> findGroupByName(String groupname) {
        return em.createQuery("SELECT g FROM GroupMaster g WHERE g.groupname = :groupname", GroupMaster.class)
                .setParameter("groupname", groupname)
                .getResultList();
    }

    @Override
    public Collection<Book> searchBooks(String searchBookname, String searchAuthor, String searchBooktype) {

        String jpql = "SELECT b FROM Book b WHERE 1=1";

        if (searchBookname != null && !searchBookname.isEmpty()) {
            jpql += " AND LOWER(b.bookname) LIKE LOWER(:bn)";
        }
        if (searchAuthor != null && !searchAuthor.isEmpty()) {
            jpql += " AND LOWER(b.authorname) LIKE LOWER(:au)";
        }
        if (searchBooktype != null && !searchBooktype.isEmpty()) {
            jpql += " AND LOWER(b.booktypeId.type) LIKE LOWER(:bt)";
        }

        var q = em.createQuery(jpql, Book.class);

        if (searchBookname != null && !searchBookname.isEmpty()) {
            q.setParameter("bn", "%" + searchBookname + "%");
        }
        if (searchAuthor != null && !searchAuthor.isEmpty()) {
            q.setParameter("au", "%" + searchAuthor + "%");
        }
        if (searchBooktype != null && !searchBooktype.isEmpty()) {
            q.setParameter("bt", "%" + searchBooktype + "%");
        }

        return q.getResultList();
    }

    @Override
    public String hashPassword(String password) {
        try {
            java.security.MessageDigest md = java.security.MessageDigest.getInstance("SHA-256");
            byte[] hash = md.digest(password.getBytes("UTF-8"));
            StringBuilder sb = new StringBuilder();
            for (byte b : hash) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    // ================= RESET PASSWORD (EMAIL) =================
    @PermitAll
    @Override
    public void resetPassword(String email, String newPassword) {
        try {
            User user = em.createQuery("SELECT u FROM User u WHERE u.email = :email", User.class)
                    .setParameter("email", email)
                    .getSingleResult();

            if (user != null) {
                user.setPassword(hashPassword(newPassword)); // ALWAYS HASH
                em.merge(user);
            }

        } catch (Exception e) {
            System.out.println("Email not found for reset: " + email);
        }
    }

    @PermitAll
    @Override
    public boolean changePassword(Integer userId, String oldPassword, String newPassword) {

        User user = em.find(User.class, userId);

        if (user == null) {
            return false;
        }

        String hashedOld = hashPassword(oldPassword);

        // check old password match
        if (!user.getPassword().equals(hashedOld)) {
            return false;  // old password incorrect
        }

        // update to new hashed password
        user.setPassword(hashPassword(newPassword));
        em.merge(user);
        return true;
    }

    @Override
    public boolean isEmailExists(String email) {
        try {
            User u = em.createQuery("SELECT u FROM User u WHERE u.email = :email", User.class)
                    .setParameter("email", email)
                    .getSingleResult();
            return u != null;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public boolean isPhoneExists(String phone) {
        try {
            User u = em.createQuery("SELECT u FROM User u WHERE u.phone = :phone", User.class)
                    .setParameter("phone", phone)
                    .getSingleResult();
            return u != null;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public boolean isUsernameExists(String username) {
        try {
            User u = em.createQuery("SELECT u FROM User u WHERE u.username = :uname", User.class)
                    .setParameter("uname", username)
                    .getSingleResult();
            return u != null;
        } catch (Exception e) {
            return false;
        }
    }

}
