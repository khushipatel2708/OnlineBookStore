package beans;

import EJB.AdminSessionBeanLocal;
import Entity.City;
import jakarta.ejb.EJB;
import jakarta.enterprise.context.SessionScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import java.io.Serializable;
import java.util.Collection;

@Named("cityBean")
@SessionScoped   // <-- important: CRUD must stay in same page
public class CityCDIBean implements Serializable {

    @EJB
    private AdminSessionBeanLocal admin;

    @Inject
    private LoginBean loginBean;

    private Integer id;
    private String name;
    private boolean editMode = false;

    // CHECK ROLE
    public boolean isAdmin() {
        return "Admin".equalsIgnoreCase(loginBean.getRole());
    }

    // FETCH ALL CITIES
    public Collection<City> getAllCities() {
        if (!isAdmin()) return null;
        return admin.getAllCities();
    }

    // ADD CITY
    public String addCity() {
        admin.addCity(name);
        name = "";
        return null;
    }

    // LOAD CITY (EDIT MODE)
    public String editCity(Integer cityId) {
        City c = admin.findCityById(cityId);
        this.id = c.getId();
        this.name = c.getName();
        this.editMode = true;
        return null;   // stay same page
    }

    // UPDATE CITY
    public String updateCity() {
        admin.updateCity(id, name);
        editMode = false;
        name = "";
        return null;
    }

    // CANCEL
    public void cancelEdit() {
        editMode = false;
        name = "";
    }

    // DELETE CITY
    public String deleteCity(Integer cityId) {
        admin.removeCity(cityId);
        return null;
    }

    // GETTERS + SETTERS
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public boolean isEditMode() { return editMode; }
    public void setEditMode(boolean editMode) { this.editMode = editMode; }
}
