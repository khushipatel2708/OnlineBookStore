package beans;

import Entity.Shipping;
import Entity.City;
import Entity.User;
import client.UserClient;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.SessionScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import java.io.Serializable;
import java.util.List;

@Named("shippingBean")
@SessionScoped
public class ShippingBean implements Serializable {

    @Inject
    private LoginBean loginBean;

    private UserClient client = new UserClient();

    private Shipping shipping = new Shipping();   // form model
    private boolean isExisting = false;           // check if record exists

    // -----------------------------
    // INIT – load shipping if user already saved earlier
    // -----------------------------
    @PostConstruct
    public void init() {
        loadShipping();
    }

    public void loadShipping() {
        try {
            if (loginBean.getUserid() == null) return;

            // Call API to get all shipping for this user
            Shipping[] list = client.getAllShipping(Shipping[].class);

            // Filter for logged-in user
            for (Shipping s : list) {
                if (s.getUserid().getId().equals(loginBean.getUserid())) {
                    shipping = s;
                    isExisting = true;     // user already has shipping
                    return;
                }
            }

            // If not found → new shipping
            shipping = new Shipping();
            isExisting = false;

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // -----------------------------
    // SAVE OR UPDATE
    // -----------------------------
    public String saveShipping() {

        try {
            // Attach current user
            User u = new User();
            u.setId(loginBean.getUserid());
            shipping.setUserid(u);

            if (isExisting) {
                // update existing
                client.updateShipping(shipping, shipping.getId().toString());
            } else {
                // create new
                client.addShipping(shipping);
            }

            // reload state
            loadShipping();

            return "UserBookList.xhtml?faces-redirect=true";

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // -----------------------------
    // GETTERS - SETTERS
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
}
