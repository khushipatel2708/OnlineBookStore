/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB40/SessionLocal.java to edit this template
 */
package EJB;

import Entity.Book;
import Entity.Booktype;
import Entity.City;
import jakarta.ejb.Local;
import java.util.List;

/**
 *
 * @author KHUSHI PC
 */
@Local
public interface AdminSessionBeanLocal {
    //BookType
    boolean create(Booktype booktype);
    boolean update(Booktype booktype);
    boolean delete(Integer id);
    Booktype find(Integer id);
    List<Booktype> findAll();
    
    //Book Method
    void addBook(Book book);
    void updateBook(Book book);
    void deleteBook(int bookId);
    Book getBookById(int bookId);
    List<Book> getAllBooks();
    List<Booktype> getAllBookTypes();
    public List<Book> getBooksByType(int typeId);
    
    //City Method
    void addCity(City city);
    void updateCity(City city);
    void deleteCity(int cityid);
    City getCityById(int cityid);
    List<City> getAllCities();
}
