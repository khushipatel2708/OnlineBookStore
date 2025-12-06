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
import java.util.Collection;

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

        // ✅ Pre-fill cityId from saved shipping
        if (shipping.getCityid() != null) {
            cityId = shipping.getCityid().getId();
        } else {
            cityId = null; // optional
        }
    }

    public Collection<Shipping> getAllShippingList() {
    return userSessionBean.getAllShippings();
}
    
    // -----------------------------
    // ⭐ SAVE or UPDATE SHIPPING
    // -----------------------------
    public String saveShipping() {

        // Attach selected city
        if (cityId != null) {
            City c = new City();
            c.setId(cityId);
            shipping.setCityid(c);
        }

        Integer selectedCityId = (shipping.getCityid() != null) ? shipping.getCityid().getId() : null;

        if (shipping.getId() == null) {
            // Add new shipping
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
            // Update shipping
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

        shipping = loginBean.getLoggedUserShipping();

        if (shipping == null) {
            shipping = new Shipping();
            isExisting = false;
        } else {
            isExisting = true;
        }

        // ⭐ IMPORTANT: initialize city object
        if (shipping.getCityid() == null) {
            shipping.setCityid(new City());
        }

        return "shippingForm.xhtml?faces-redirect=true";
    }

    public Integer getCityId() {
        return cityId;
    }

    public void setCityId(Integer cityId) {
        this.cityId = cityId;
    }

}
