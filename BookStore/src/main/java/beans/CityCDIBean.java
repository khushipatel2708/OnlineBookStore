package beans;

import Entity.City;
import client.MyAdminClient;
import jakarta.enterprise.context.SessionScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

@Named("cityBean")
@SessionScoped
public class CityCDIBean implements Serializable {

    private MyAdminClient adminClient = new MyAdminClient();

    @Inject
    private LoginBean loginBean;

    private Integer id;
    private String name;
    private boolean editMode = false;

    public boolean isAdmin() {
        return "Admin".equalsIgnoreCase(loginBean.getRole());
    }

    public Collection<City> getAllCities() {
        return adminClient.getAllCities(Collection.class);
    }

    // RESET FORM FOR ADD CITY
    public String resetForm() {
        id = null;
        name = "";
        editMode = false;
        return null;
    }

    // ADD CITY
    public String addCity() {
        City city = new City();
        city.setName(name);

        adminClient.addCity(city);

        resetForm();
        return "/city.xhtml?faces-redirect=true";  // FIX
    }

    // LOAD CITY FOR EDIT
    public String editCity(Integer cityId) {
        City c = adminClient.getCityById(City.class, cityId.toString());

        this.id = c.getId();
        this.name = c.getName();
        this.editMode = true;

        return null;
    }

    // UPDATE CITY
    public String updateCity() {
        City city = new City();
        city.setName(name);

        adminClient.updateCity(city, id.toString());

        resetForm();
        return "/city.xhtml?faces-redirect=true";  // FIX
    }

    // CANCEL EDIT
    public void cancelEdit() {
        resetForm();
    }

    // DELETE
    public String deleteCity(Integer cityId) {
        adminClient.deleteCity(cityId.toString());
        return null;
    }

    // GETTERS/SETTERS
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isEditMode() {
        return editMode;
    }

    public void setEditMode(boolean editMode) {
        this.editMode = editMode;
    }

    private int pageSize = 5;      // Number of rows per page
    private int pageNumber = 1;    // Current page

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getPageNumber() {
        return pageNumber;
    }

    public void setPageNumber(int pageNumber) {
        this.pageNumber = pageNumber;
    }

    public int getTotalPages() {
        int totalGroups = getAllCities().size();
        return (int) Math.ceil((double) totalGroups / pageSize);
    }

    public Collection<City> getPagedGroups() {
        List<City> all = new ArrayList<>(getAllCities());
        int fromIndex = (pageNumber - 1) * pageSize;
        int toIndex = Math.min(fromIndex + pageSize, all.size());
        if (fromIndex > all.size()) {
            return Collections.emptyList();
        }
        return all.subList(fromIndex, toIndex);
    }

    public void nextPage() {
        if (pageNumber < getTotalPages()) {
            pageNumber++;
        }
    }

    public void previousPage() {
        if (pageNumber > 1) {
            pageNumber--;
        }
    }

}
