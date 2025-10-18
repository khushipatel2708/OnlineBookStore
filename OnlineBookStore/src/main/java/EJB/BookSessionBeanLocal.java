/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB40/SessionLocal.java to edit this template
 */
package EJB;

import Entity.Book;
import Entity.Booktype;
import jakarta.ejb.Local;
import java.util.List;

/**
 *
 * @author KHUSHI PC
 */
@Local
public interface BookSessionBeanLocal {
  void addBook(Book book);
    void updateBook(Book book);
    void deleteBook(int bookId);
    Book getBookById(int bookId);
    List<Book> getAllBooks();
    List<Booktype> getAllBookTypes();

}
