package beans;

import EJB.UserSessionBeanLocal;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.enterprise.context.RequestScoped;

@Named
@RequestScoped
public class PaymentSuccessBean {

    @Inject
    private UserSessionBeanLocal userEJB;

    @Inject
    private LoginBean loginBean;

    @Inject
    private PaymentBean paymentBean;

    public String updateAndClear() {
        int uid = loginBean.getLoggedInUser().getId();

        // Update payment status
        userEJB.updatePaymentStatus(uid, "Pending", "Paid");

        // Clear Cart
        paymentBean.clearCartAfterSuccess();

        return "user.xhtml?faces-redirect=true";
    }
}
