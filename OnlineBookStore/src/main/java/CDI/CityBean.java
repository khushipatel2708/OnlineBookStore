package CDI;

import EJB.AdminSessionBeanLocal;
import Entity.City;
import jakarta.ejb.EJB;
import jakarta.enterprise.context.SessionScoped;
import jakarta.inject.Named;
import java.io.Serializable;
import java.util.List;

@Named(value = "cityBean")
@SessionScoped // ✅ keeps data during navigation
public class CityBean implements Serializable {

    @EJB
    private AdminSessionBeanLocal citySessionBean;

    private int cityid;
    private String cityname;

    // Getters & Setters
    public int getCityid() {
        return cityid;
    }

    public void setCityid(int cityid) {
        this.cityid = cityid;
    }

    public String getCityname() {
        return cityname;
    }

    public void setCityname(String cityname) {
        this.cityname = cityname;
    }

    // Get all cities
    public List<City> getAllCities() {
        return citySessionBean.getAllCities();
    }

    // Add or Edit city
    public String saveCity() {
        City c = new City();
        c.setCityname(cityname);

        if (cityid == 0) { 
            // Add mode
            citySessionBean.addCity(c);
        } else {
            // Edit mode
            c.setCityid(cityid);
            citySessionBean.updateCity(c);
        }

        // Reset fields
        cityid = 0;
        cityname = "";
        
        return "cityList.xhtml?faces-redirect=true";
    }

    // Load city for edit
    public String editCity(int id) {
        City c = citySessionBean.getCityById(id);
        if (c != null) {
            this.cityid = c.getCityid();
            this.cityname = c.getCityname();
        }
        return "cityForm.xhtml?faces-redirect=true";
    }

    // Delete
    public void deleteCity(int id) {
        citySessionBean.deleteCity(id);
    }
}
