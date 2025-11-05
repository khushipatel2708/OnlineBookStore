package EJB;

import Entity.City;
import jakarta.ejb.Local;
import java.util.List;

@Local
public interface CitySessionBeanLocal {
    void addCity(City city);
    void updateCity(City city);
    void deleteCity(int cityid);
    City getCityById(int cityid);
    List<City> getAllCities();
}
