/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB40/SessionLocal.java to edit this template
 */
package EJB;

import Entity.Book;
import jakarta.ejb.Local;
import java.util.Collection;

/**
 *
 * @author KHUSHI PC
 */
@Local
public interface AdminSessionBeanLocal {
    Collection<Book> getAllBook();
}
