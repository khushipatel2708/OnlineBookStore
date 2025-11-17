package beans;

import Entity.City;
import client.MyAdminClient;
import jakarta.enterprise.context.SessionScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;

import java.io.Serializable;
import java.util.Collection;

@Named("cityBean")
@SessionScoped
public class CityCDIBean implements Serializable {

    private MyAdminClient adminClient = new MyAdminClient();  // USE CLIENT ONLY

    @Inject
    private LoginBean loginBean;

    private Integer id;
    private String name;
    private boolean editMode = false;

    // CHECK ROLE
    public boolean isAdmin() {
        return "Admin".equalsIgnoreCase(loginBean.getRole());
    }

    // ===================== GET ALL CITIES ======================
    public Collection<City> getAllCities() {
        if (!isAdmin()) return null;
        return adminClient.getAllCities(Collection.class);
    }

    // ===================== ADD CITY ===========================
    public String addCity() {

        City city = new City();
        city.setName(name);

        adminClient.addCity(city);   // POST call

        name = "";
        return null;
    }

    // ===================== EDIT CITY ===========================
    public String editCity(Integer cityId) {

        City c = adminClient.getCityById(City.class, cityId.toString());

        this.id = c.getId();
        this.name = c.getName();
        this.editMode = true;

        return null;
    }

    // ===================== UPDATE CITY =========================
    public String updateCity() {

        City c = new City();
        c.setName(name);

        adminClient.updateCity(c, id.toString());   // PUT call

        editMode = false;
        name = "";
        return null;
    }

    // ===================== CANCEL EDIT =========================
    public void cancelEdit() {
        editMode = false;
        name = "";
    }

    // ===================== DELETE CITY =========================
    public String deleteCity(Integer cityId) {
        adminClient.deleteCity(cityId.toString());   // DELETE call
        return null;
    }

    // ===================== GETTERS / SETTERS ===================
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public boolean isEditMode() { return editMode; }
    public void setEditMode(boolean editMode) { this.editMode = editMode; }
}
