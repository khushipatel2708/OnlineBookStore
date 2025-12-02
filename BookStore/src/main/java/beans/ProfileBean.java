package beans;

import Entity.User;
import client.MyAdminClient;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.SessionScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import java.io.Serializable;

@Named
@SessionScoped
public class ProfileBean implements Serializable {

    @Inject
    private LoginBean loginBean;

    private MyAdminClient client = new MyAdminClient();

    private User user;  // Profile data

    @PostConstruct
    public void init() {
        loadProfile();
    }

    public void loadProfile() {
        try {

            if (loginBean != null && loginBean.getUserid() != null) {

                System.out.println("Loading profile for User ID: " + loginBean.getUserid());

                user = client.getUserById(User.class, loginBean.getUserid().toString());
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String updateProfile() {
        try {
            client.updateUser(user, user.getId().toString());

        } catch (Exception e) {
            e.printStackTrace();
        }

        return "profile.xhtml?faces-redirect=true";
    }

    // GETTER - SETTER
    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
