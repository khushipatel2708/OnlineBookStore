package beans;

import EJB.AdminSessionBeanLocal;
import Entity.City;
import jakarta.inject.Named;
import jakarta.enterprise.context.RequestScoped;
import jakarta.ejb.EJB;
import java.util.Collection;

@Named("cityBean")
@RequestScoped
public class CityCDIBean {

    @EJB
    AdminSessionBeanLocal admin;

    private Integer id;
    private String name;

    private City selectedCity;   // ✅ REQUIRED

    public City getSelectedCity() {
        return selectedCity;
    }

    public void setSelectedCity(City selectedCity) {
        this.selectedCity = selectedCity;
    }

    // ----- Get all cities -----
    public Collection<City> getAllCities() {
        return admin.getAllCities();
    }

    // ----- Save / Update -----
    public String saveCity() {
        if (selectedCity == null) {   // ADD NEW
            admin.addCity(name);
        } else {                      // UPDATE
            admin.updateCity(selectedCity.getId(), name);
        }
        clearForm();
        return "city.xhtml?faces-redirect=true";
    }

    // ----- Edit -----
    public String editCity(City c) {
        selectedCity = c;
        name = c.getName();
        return null;
    }

    // ----- Delete -----
    public String deleteCity(Integer id) {
        admin.removeCity(id);
        return "city.xhtml?faces-redirect=true";
    }

    // ----- Clear form -----
    public void clearForm() {
        selectedCity = null;
        name = "";
    }

    // ----- Getters/Setters -----
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
}
