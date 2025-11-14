package beans;

import EJB.AdminSessionBeanLocal;
import Entity.City;
import jakarta.inject.Named;
import jakarta.enterprise.context.RequestScoped;
import jakarta.ejb.EJB;
import jakarta.faces.view.ViewScoped;
import java.io.Serializable;
import java.util.Collection;

@Named("cityBean")
@ViewScoped
public class CityCDIBean implements Serializable {

    @EJB
    AdminSessionBeanLocal admin;

    private Integer id;
    private String name;
    private City selectedCity;

    public City getSelectedCity() { return selectedCity; }
    public void setSelectedCity(City selectedCity) { this.selectedCity = selectedCity; }

    public Collection<City> getAllCities() {
        return admin.getAllCities();
    }

    public String saveCity() {
        if (selectedCity == null) {      // ADD
            admin.addCity(name);
        } else {                         // UPDATE
            admin.updateCity(selectedCity.getId(), name);
        }
        clearForm();
        return "city.xhtml?faces-redirect=true";
    }

    public void editCity(City c) {
        this.selectedCity = c;
        this.name = c.getName();
    }

    public void deleteCity(Integer id) {
        admin.removeCity(id);
    }

    public void clearForm() {
        selectedCity = null;
        name = "";
    }

    // Getters/Setters...

    // ----- Getters/Setters -----
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
}
