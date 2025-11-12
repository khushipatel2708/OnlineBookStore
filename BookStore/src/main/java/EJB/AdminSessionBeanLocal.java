/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB40/SessionLocal.java to edit this template
 */
package EJB;

import Entity.Book;
import Entity.Booktype;
import Entity.City;
import Entity.GroupMaster;
import Entity.User;
import jakarta.ejb.Local;
import java.util.Collection;

/**
 *
 * @author KHUSHI PC
 */
@Local
public interface AdminSessionBeanLocal {

    // ---------- USER ----------
    Collection<User> getAllUsers();
    User findUserById(Integer id);
    void addUser(String fullname, String phone, String username, String email, String password, String status, Integer groupId);
    void updateUser(Integer id, String fullname, String phone, String username, String email, String password, String status, Integer groupId);
    void deleteUser(Integer id);
    User findUserByUsername(String username);


    // ---------- Book Type----------
    Collection<Booktype> getAllBooktypes();
    Booktype getBooktypeById(Integer id);
    void addBooktype(String type, String description);
    void updateBooktype(Integer id, String type, String description);
    void deleteBooktype(Integer id);
    
    // ---------- Book ----------
    Collection<Book> getAllBooks();
    void addBook(String bookname, String authorname, Double price, Integer booktypeId,
             String coverPhoto, String frontPagePhoto, String lastPagePhoto);

    void updateBook(Integer id, String bookname, String authorname, Double price, Integer booktypeId,
                String coverPhoto, String frontPagePhoto, String lastPagePhoto);

    void deleteBook(Integer id);
    Book findBookById(Integer id);

    //-------------city------------
    Collection<City> getAllCities();
    void addCity(String name);
    void updateCity(Integer id, String name);
    void removeCity(Integer id);
    City findCityById(Integer id);
    Collection<City> findCityByName(String name);

    //--------------Group Master-----------------
    void addGroup(String groupname, String username);
    void updateGroup(Integer id, String groupname, String username);
    void removeGroup(Integer id);
    GroupMaster findGroupById(Integer id);
    Collection<GroupMaster> findGroupByName(String groupname);
    Collection<GroupMaster> getAllGroups();
    
//    void deleteShipping(Integer id);

}
