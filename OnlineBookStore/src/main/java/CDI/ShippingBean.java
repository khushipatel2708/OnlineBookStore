package CDI;

import EJB.ShippingSessionBeanLocal;
import EJB.CitySessionBeanLocal;
import Entity.City;
import Entity.Shipping;
import Entity.User;
import jakarta.ejb.EJB;
import jakarta.enterprise.context.SessionScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import java.io.Serializable;
import java.util.List;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;

@Named(value = "shippingBean")
@SessionScoped
public class ShippingBean implements Serializable {

    @EJB
    private ShippingSessionBeanLocal shippingSessionBean;

    @EJB
    private CitySessionBeanLocal citySessionBean;

    @Inject
    private LoginBean loginBean;

    private int id;
    private String name;
    private String phone;
    private String address1;
    private String address2;
    private String landmark;
    private String pincode;
    private int cityid;

    // ✅ Getters/Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getAddress1() { return address1; }
    public void setAddress1(String address1) { this.address1 = address1; }

    public String getAddress2() { return address2; }
    public void setAddress2(String address2) { this.address2 = address2; }

    public String getLandmark() { return landmark; }
    public void setLandmark(String landmark) { this.landmark = landmark; }

    public String getPincode() { return pincode; }
    public void setPincode(String pincode) { this.pincode = pincode; }

    public int getCityid() { return cityid; }
    public void setCityid(int cityid) { this.cityid = cityid; }

    // ✅ Load all cities
    public List<City> getAllCities() {
        return citySessionBean.getAllCities();
    }

    // ✅ Get shipping list for logged-in user
    public List<Shipping> getUserShippingList() {
        User user = loginBean.getLoggedInUser();
        if (user != null)
            return shippingSessionBean.getShippingByUser(user.getId());
        return null;
    }

    // ✅ Save or update
    public String saveShipping() {
        try {
            User user = loginBean.getLoggedInUser();
            if (user == null) {
                FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "User not logged in!", null));
                return null;
            }

            City c = citySessionBean.getCityById(cityid);
            if (c == null) {
                FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Invalid City selected!", null));
                return null;
            }

            Shipping s = new Shipping();
            s.setId(id);
            s.setUserid(user.getId());
            s.setName(name);
            s.setPhone(phone);
            s.setAddress1(address1);
            s.setAddress2(address2);
            s.setLandmark(landmark);
            s.setCity(c);
            s.setPincode(pincode);

            if (id == 0)
                shippingSessionBean.addShipping(s);
            else
                shippingSessionBean.updateShipping(s);

            clearForm();
            return "shippingList.xhtml?faces-redirect=true";

        } catch (Exception e) {
            e.printStackTrace();
            FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error: " + e.getMessage(), null));
            return null;
        }
    }

    // ✅ Edit existing record
    public String editShipping(int sid) {
        Shipping s = shippingSessionBean.getShippingById(sid);
        if (s != null) {
            this.id = s.getId();
            this.name = s.getName();
            this.phone = s.getPhone();
            this.address1 = s.getAddress1();
            this.address2 = s.getAddress2();
            this.landmark = s.getLandmark();
            this.cityid = s.getCity().getCityid();
            this.pincode = s.getPincode();
        }
        return "shippingForm.xhtml?faces-redirect=true";
    }

    // ✅ Delete
    public void deleteShipping(int sid) {
        shippingSessionBean.deleteShipping(sid);
    }

    // ✅ Clear form fields
    private void clearForm() {
        this.id = 0;
        this.name = "";
        this.phone = "";
        this.address1 = "";
        this.address2 = "";
        this.landmark = "";
        this.pincode = "";
        this.cityid = 0;
    }
}
