package CDI;
import jakarta.inject.Named;
import jakarta.enterprise.context.Dependent;


import Entities.User;
import jakarta.enterprise.context.SessionScoped;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Named;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.io.Serializable;

@Named
@SessionScoped
public class SessionUtils implements Serializable {

    public void checkSession() throws IOException {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        HttpSession session = (HttpSession) facesContext.getExternalContext().getSession(false);

        if (session == null || session.getAttribute("loggedUser") == null) {
            // ❌ No session or not logged in → redirect to login
            facesContext.getExternalContext().redirect("login.xhtml");
            return;
        }

        User logged = (User) session.getAttribute("loggedUser");
        String currentPage = facesContext.getViewRoot().getViewId();

        // ✅ Prevent wrong page access
        if (currentPage.contains("admin") && 
            !logged.getGroupMaster().getGroupName().equalsIgnoreCase("Admin")) {
            facesContext.getExternalContext().redirect("user.xhtml");
        } else if (currentPage.contains("user") && 
                   !logged.getGroupMaster().getGroupName().equalsIgnoreCase("User")) {
            facesContext.getExternalContext().redirect("admin.xhtml");
        }
    }
}
