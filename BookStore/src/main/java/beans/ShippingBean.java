package beans;

import EJB.UserSessionBeanLocal;
import Entity.City;
import Entity.Shipping;
import jakarta.annotation.PostConstruct;
import jakarta.ejb.EJB;
import jakarta.enterprise.context.SessionScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import java.io.Serializable;

@Named("shippingBean")
@SessionScoped
public class ShippingBean implements Serializable {

    @EJB
    private UserSessionBeanLocal userSessionBean;

    @Inject
    private LoginBean loginBean;
    private boolean isExisting;

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
    }

    // -----------------------------
    // ⭐ SAVE or UPDATE SHIPPING
    // -----------------------------
public String saveShipping() {

    if (shipping.getId() == null) {
        // Add new shipping without city
        userSessionBean.addShipping(
            loginBean.getLoggedInUser().getId(),
            null, // cityId removed
            shipping.getName(),
            shipping.getPhone(),
            shipping.getAddress1(),
            shipping.getAddress2(),
            shipping.getLandmark(),
            shipping.getPincode()
        );
    } else {
        // Update shipping without city
        userSessionBean.updateShipping(
            shipping.getId(),
            loginBean.getLoggedInUser().getId(),
            null, // cityId removed
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

}
