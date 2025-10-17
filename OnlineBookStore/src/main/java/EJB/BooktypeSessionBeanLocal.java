/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB40/SessionLocal.java to edit this template
 */
package EJB;

import Entity.Booktype;
import jakarta.ejb.Local;
import java.util.List;

/**
 *
 * @author KHUSHI PC
 */
@Local
public interface BooktypeSessionBeanLocal {
       boolean create(Booktype booktype);
    boolean update(Booktype booktype);
    boolean delete(Integer id);
    Booktype find(Integer id);
    List<Booktype> findAll();
}
