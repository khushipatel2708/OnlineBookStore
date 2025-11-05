package EJB;

import Entity.City;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.List;

@Stateless
public class CitySessionBean implements CitySessionBeanLocal {

    @PersistenceContext(unitName = "my_pu")
    EntityManager em;

    @Override
    public void addCity(City city) {
        em.persist(city);
    }

    @Override
    public void updateCity(City city) {
        em.merge(city);
    }

    @Override
    public void deleteCity(int cityid) {
        City c = em.find(City.class, cityid);
        if (c != null) {
            em.remove(c);
        }
    }

    @Override
    public City getCityById(int cityid) {
        return em.find(City.class, cityid);
    }

    @Override
    public List<City> getAllCities() {
        return em.createQuery("SELECT c FROM City c", City.class).getResultList();
    }
}
