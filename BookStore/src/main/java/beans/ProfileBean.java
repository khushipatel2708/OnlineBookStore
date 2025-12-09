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
    private User user;

    @PostConstruct
    public void init() {
        // optional, in case user returns to page later
        if (loginBean != null && loginBean.getUserid() != null) {
            loadProfile();
        }
    }

    public void loadProfile() {
        try {
            if (loginBean != null && loginBean.getUserid() != null) {
                user = client.getUserById(User.class, loginBean.getUserid().toString());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String updateProfile() {
        try {
            if (user != null && user.getId() != null) {
                client.updateUser(user, user.getId().toString());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "UserProfile.xhtml?faces-redirect=true";
    }

    public User getUser() {
        if (user == null) {
            loadProfile();
            if (user == null) user = new User();
        }
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
