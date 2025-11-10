/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB40/SessionLocal.java to edit this template
 */
package EJB;

import Entity.Book;
import Entity.Booktype;
import Entity.City;
import jakarta.ejb.Local;
import java.util.Collection;

/**
 *
 * @author KHUSHI PC
 */
@Local
public interface AdminSessionBeanLocal {
     // ---------- Book Type----------

    Collection<Booktype> getAllBooktypes();
    Booktype getBooktypeById(Integer id);
    void addBooktype(String type, String description);
    void updateBooktype(Integer id, String type, String description);
    void deleteBooktype(Integer id);
    Booktype findBooktypeById(Integer id);

    // ---------- Book ----------
    Collection<Book> getAllBooks();
    void addBook(String name, String author, Double price, Integer booktypeId);
    void updateBook(Integer id, String name, String author, Double price, Integer booktypeId);
    void deleteBook(Integer id);
    Book findBookById(Integer id);
    
    //-------------city------------
    Collection<City> getAllCities();
    void addCity(String name);
    void updateCity(Integer id, String name);
    void removeCity(Integer id);
    City findCityById(Integer id);
    Collection<City> findCityByName(String name);
}
