/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB40/StatelessEjbClass.java to edit this template
 */
package EJB;

import Entity.Book;
import Entity.Booktype;
import Entity.City;
import Entity.GroupMaster;
import Entity.User;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.math.BigDecimal;
import java.util.Collection;

@Stateless
public class AdminSessionBean implements AdminSessionBeanLocal {

    @PersistenceContext(unitName = "my_pu")
    EntityManager em;
    //===============User=====================

    @Override
    public Collection<User> getAllUsers() {
        return em.createNamedQuery("User.findAll", User.class).getResultList();
    }

    @Override
    public User findUserById(Integer id) {
        return em.find(User.class, id);
    }

    @Override
    public void addUser(String fullname, String phone, String username, String email, String password, String status, Integer groupId) {
        GroupMaster group = em.find(GroupMaster.class, groupId);
        if (group != null) {
            User u = new User();
            u.setFullname(fullname);
            u.setPhone(phone);
            u.setUsername(username);
            u.setEmail(email);
            u.setPassword(password);
            u.setStatus(status != null ? status : "Active");
            u.setGroupid(group);
            em.persist(u);
        }
    }

    @Override
    public void updateUser(Integer id, String fullname, String phone, String username, String email, String password, String status, Integer groupId) {
        User u = em.find(User.class, id);
        if (u != null) {
            GroupMaster group = em.find(GroupMaster.class, groupId);
            u.setFullname(fullname);
            u.setPhone(phone);
            u.setUsername(username);
            u.setEmail(email);
            u.setPassword(password);
            u.setStatus(status);
            u.setGroupid(group);
            em.merge(u);
        }
    }

    @Override
    public void deleteUser(Integer id) {
        User u = em.find(User.class, id);
        if (u != null) {
            em.remove(u);
        }
    }

    // ---------- BOOKTYPE CRUD ----------
    @Override
    public Collection<Booktype> getAllBooktypes() {
        return em.createNamedQuery("Booktype.findAll").getResultList();
    }

    @Override
    public Booktype getBooktypeById(Integer id) {
        return em.find(Booktype.class, id);
    }

    @Override
    public void addBooktype(String type, String description) {
        Booktype bt = new Booktype();
        bt.setType(type);
        bt.setDescription(description);
        em.persist(bt);
    }

    @Override
    public void updateBooktype(Integer id, String type, String description) {
        Booktype bt = em.find(Booktype.class, id);
        if (bt != null) {
            bt.setType(type);
            bt.setDescription(description);
            em.merge(bt);
        }
    }

    @Override
    public void deleteBooktype(Integer id) {
        Booktype bt = em.find(Booktype.class, id);
        if (bt != null) {
            em.remove(bt);
        }
    }

    @Override
    public Booktype findBooktypeById(Integer id) {
        return em.find(Booktype.class, id);
    }

    // ---------- BOOK CRUD ----------
    @Override
    public Collection<Book> getAllBooks() {
        return em.createNamedQuery("Book.findAll").getResultList();
    }

    @Override
    public void addBook(String bookname, String authorname, Double price, Integer booktypeId,
                        String coverPhoto, String frontPagePhoto, String lastPagePhoto) {
        Booktype bt = em.find(Booktype.class, booktypeId);
        if (bt != null) {
            Book b = new Book();
            b.setBookname(bookname);
            b.setAuthorname(authorname);
            b.setPrice(BigDecimal.valueOf(price));
            b.setBooktypeId(bt);
            b.setAvailable(Boolean.TRUE);
            b.setCoverPhoto(coverPhoto);
            b.setFrontPagePhoto(frontPagePhoto);
            b.setLastPagePhoto(lastPagePhoto);
            em.persist(b);
        }
    }

    @Override
    public void updateBook(Integer id, String bookname, String authorname, Double price, Integer booktypeId,
                           String coverPhoto, String frontPagePhoto, String lastPagePhoto) {
        Book b = em.find(Book.class, id);
        if (b != null) {
            Booktype bt = em.find(Booktype.class, booktypeId);
            b.setBookname(bookname);
            b.setAuthorname(authorname);
            b.setPrice(BigDecimal.valueOf(price));
            b.setBooktypeId(bt);
            b.setCoverPhoto(coverPhoto);
            b.setFrontPagePhoto(frontPagePhoto);
            b.setLastPagePhoto(lastPagePhoto);
            em.merge(b);
        }
    }


    @Override
    public void deleteBook(Integer id) {
        Book b = em.find(Book.class, id);
        if (b != null) {
            em.remove(b);
        }
    }

    @Override
    public Book findBookById(Integer id) {
        return em.find(Book.class, id);
    }

    //--------city---------------------
    @Override
    public Collection<City> getAllCities() {
        return em.createNamedQuery("City.findAll").getResultList();
    }

    @Override
    public void addCity(String name) {
        City city = new City();
        city.setName(name);
        em.persist(city);
    }

    @Override
    public void updateCity(Integer id, String name) {
        City city = em.find(City.class, id);
        if (city != null) {
            city.setName(name);
            em.merge(city);
        }
    }

    @Override
    public void removeCity(Integer id) {
        City city = em.find(City.class, id);
        if (city != null) {
            em.remove(city);
        }
    }

    @Override
    public City findCityById(Integer id) {
        return em.find(City.class, id);
    }

    @Override
    public Collection<City> findCityByName(String name) {
        return em.createNamedQuery("City.findByName")
                .setParameter("name", name)
                .getResultList();
    }

    @Override
    public void addGroup(String groupname, String username) {
        GroupMaster g = new GroupMaster();
        g.setGroupname(groupname);
        g.setUsername(username);
        em.persist(g);
    }

    @Override
    public void updateGroup(Integer id, String groupname, String username) {
        GroupMaster g = em.find(GroupMaster.class, id);
        if (g != null) {
            g.setGroupname(groupname);
            g.setUsername(username);
            em.merge(g);
        }
    }

    @Override
    public void removeGroup(Integer id) {
        GroupMaster g = em.find(GroupMaster.class, id);
        if (g != null) {
            em.remove(g);
        }
    }

    @Override
    public GroupMaster findGroupById(Integer id) {
        return em.find(GroupMaster.class, id);
    }

    @Override
    public Collection<GroupMaster> findGroupByName(String groupname) {
        return em.createNamedQuery("GroupMaster.findByGroupname", GroupMaster.class)
                .setParameter("groupname", groupname)
                .getResultList();
    }

    @Override
    public Collection<GroupMaster> getAllGroups() {
        return em.createNamedQuery("GroupMaster.findAll", GroupMaster.class)
                .getResultList();
    }

}
