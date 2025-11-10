/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB40/SessionLocal.java to edit this template
 */
package EJB;

import Entity.Book;
import Entity.City;
import jakarta.ejb.Local;
import java.util.Collection;

/**
 *
 * @author KHUSHI PC
 */
@Local
public interface AdminSessionBeanLocal {
    Collection<Book> getAllBook();
    
    Collection<City> getAllCities();
    void addCity(String name);
    void updateCity(Integer id, String name);
    void removeCity(Integer id);
    City findCityById(Integer id);
    Collection<City> findCityByName(String name);
}
