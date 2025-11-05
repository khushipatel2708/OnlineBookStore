package CDI;

import EJB.UserSessionBeanLocal;
import Entity.User;
import jakarta.ejb.EJB;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Named;
import jakarta.faces.context.FacesContext;
import java.io.IOException;
import java.io.Serializable;

@Named(value = "profileBean")
@RequestScoped
public class ProfileBean implements Serializable {

    @EJB
    private UserSessionBeanLocal userBean;

    public String updateProfile() {
        try {
            User loggedInUser = (User) FacesContext.getCurrentInstance()
                    .getExternalContext().getSessionMap().get("user");

            if (loggedInUser != null) {
                userBean.updateProfile(loggedInUser);
                return "profile.xhtml?faces-redirect=true";
            } else {
                return "login.xhtml?faces-redirect=true";
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public String goBack() throws IOException {
        User loggedInUser = (User) FacesContext.getCurrentInstance()
                .getExternalContext().getSessionMap().get("user");
        if (loggedInUser.getGroupid().getGroupname().equalsIgnoreCase("admin")) {
            return "adminPage.xhtml?faces-redirect=true";
        } else {
            return "userPage.xhtml?faces-redirect=true";
        }
    }
}
