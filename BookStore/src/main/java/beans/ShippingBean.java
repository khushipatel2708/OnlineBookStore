package beans;

import EJB.UserSessionBeanLocal;
import Entity.City;
import Entity.Shipping;
import client.MyAdminClient;
import jakarta.annotation.PostConstruct;
import jakarta.ejb.EJB;
import jakarta.enterprise.context.SessionScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

@Named("shippingBean")
@SessionScoped
public class ShippingBean implements Serializable {

    @EJB
    private UserSessionBeanLocal userSessionBean;

    @Inject
    private LoginBean loginBean;
    private boolean isExisting;
    private Integer cityId;

    private MyAdminClient adminClient = new MyAdminClient();
    private Shipping shipping;   // ⭐ main model

    // -----------------------------
    // ⭐ LOAD SHIPPING AFTER LOGIN
    // -----------------------------
    @PostConstruct
    public void init() {
        shipping = loginBean.getLoggedUserShipping();

        if (shipping == null) {
            shipping = new Shipping();
            isExisting = false;
        } else {
            isExisting = true;
        }

        if (shipping.getCityid() != null) {
            cityId = shipping.getCityid().getId();
        } else {
            cityId = null;
        }
    }

    public void loadShipping() {
        Integer userId = loginBean.getLoggedInUser() != null ? loginBean.getLoggedInUser().getId() : null;

        if (userId != null) {
            shipping = userSessionBean.getLatestShippingByUser(userId);
        }

        if (shipping == null) {
            shipping = new Shipping();
            isExisting = false;
        } else {
            isExisting = true;
        }

        cityId = (shipping.getCityid() != null) ? shipping.getCityid().getId() : null;

        // Optional: initialize City object to avoid null pointer
        if (shipping.getCityid() == null) {
            shipping.setCityid(new City());
        }
    }

    public Collection<Shipping> getAllShippingList() {
        return userSessionBean.getAllShippings();
    }

    // -----------------------------
    // ⭐ SAVE or UPDATE SHIPPING
    // -----------------------------
public String saveShipping() {
    if (cityId != null) {
        City c = new City();
        c.setId(cityId);
        shipping.setCityid(c);
    }

    Integer selectedCityId = (shipping.getCityid() != null) ? shipping.getCityid().getId() : null;

    if (shipping.getId() == null) {
        userSessionBean.addShipping(
            loginBean.getLoggedInUser().getId(),
            selectedCityId,
            shipping.getName(),
            shipping.getPhone(),
            shipping.getAddress1(),
            shipping.getAddress2(),
            shipping.getLandmark(),
            shipping.getPincode()
        );
    } else {
        userSessionBean.updateShipping(
            shipping.getId(),
            loginBean.getLoggedInUser().getId(),
            selectedCityId,
            shipping.getName(),
            shipping.getPhone(),
            shipping.getAddress1(),
            shipping.getAddress2(),
            shipping.getLandmark(),
            shipping.getPincode()
        );
    }

    // ✅ Reload shipping to reflect saved data immediately
    loadShipping();

    return "UserBookList.xhtml?faces-redirect=true";
}

    public Collection<City> getAllCities() {
        return adminClient.getAllCities(Collection.class);
    }

//    public String saveShipping() {
//
//        Integer cityId = null;
//        if (shipping.getCityid() != null) {
//            cityId = shipping.getCityid().getId();
//        }
//
//        if (shipping.getId() == null) {
//            userSessionBean.addShipping(
//                    loginBean.getLoggedInUser().getId(),
//                    cityId,
//                    shipping.getName(),
//                    shipping.getPhone(),
//                    shipping.getAddress1(),
//                    shipping.getAddress2(),
//                    shipping.getLandmark(),
//                    shipping.getPincode()
//            );
//        } else {
//            userSessionBean.updateShipping(
//                    shipping.getId(),
//                    loginBean.getLoggedInUser().getId(),
//                    cityId,
//                    shipping.getName(),
//                    shipping.getPhone(),
//                    shipping.getAddress1(),
//                    shipping.getAddress2(),
//                    shipping.getLandmark(),
//                    shipping.getPincode()
//            );
//        }
//
//        return "confirmOrder.jsf?faces-redirect=true";
//    }
    // -----------------------------
    // GETTERS & SETTERS
    // -----------------------------
    public Shipping getShipping() {
        return shipping;
    }

    public void setShipping(Shipping shipping) {
        this.shipping = shipping;
    }

    public boolean isIsExisting() {
        return isExisting;
    }

    public String goToShippingPage() {
        loadShipping();
        return "shippingForm.xhtml?faces-redirect=true";
    }

    public Integer getCityId() {
        return cityId;
    }

    public void setCityId(Integer cityId) {
        this.cityId = cityId;
    }
    // -----------------------------
// ⭐ DELETE SHIPPING
// -----------------------------

    public void deleteShipping(Integer shippingId) {
        if (shippingId != null) {
            userSessionBean.removeShipping(shippingId);
        }
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
        int totalGroups = getAllShippingList().size();
        return (int) Math.ceil((double) totalGroups / pageSize);
    }

    public Collection<Shipping> getPagedGroups() {
        List<Shipping> all = new ArrayList<>(getAllShippingList());
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
