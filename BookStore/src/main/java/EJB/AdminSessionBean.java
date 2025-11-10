/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB40/StatelessEjbClass.java to edit this template
 */
package EJB;

import Entity.Book;
import Entity.City;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.Collection;

@Stateless
public class AdminSessionBean implements AdminSessionBeanLocal {

    @PersistenceContext(unitName = "my_pu")
    EntityManager em;

    @Override
    public Collection<Book> getAllBook() {
        return em.createNamedQuery("Book.findAll").getResultList();
    }
    
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
    
}
