package beans;

import EJB.UserSessionBeanLocal;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.enterprise.context.RequestScoped;

@Named
@RequestScoped
public class PaymentFailureBean {

    @Inject
    private UserSessionBeanLocal userEJB;

    @Inject
    private LoginBean loginBean;

    public String updateFailure() {
        int uid = loginBean.getLoggedInUser().getId();

        // Pending → Canceled
        userEJB.updatePaymentStatus(uid, "Pending", "Canceled");

        return "user.xhtml?faces-redirect=true";
    }
}
