package beans;

import EJB.UserSessionBeanLocal;
import Entity.Orderlist;
import Entity.User;
import jakarta.ejb.EJB;
import jakarta.enterprise.context.SessionScoped;
import jakarta.inject.Named;
import java.io.Serializable;
import java.util.List;

@Named("orderBean")
@SessionScoped
public class OrderBean implements Serializable {

    @EJB
    private UserSessionBeanLocal userSessionBean;

    private List<Orderlist> orders;

    // Admin - view pending orders
    public List<Orderlist> getPendingOrders() {
        orders = userSessionBean.getPendingOrders();
        return orders;
    }

    public void deliverOrder(Integer orderId) {
        userSessionBean.markAsDelivered(orderId);
        orders = userSessionBean.getPendingOrders();
    }

    // User - view own orders
    public List<Orderlist> getUserOrders(User user) {
        orders = userSessionBean.getOrdersByUser(user);
        return orders;
    }

    public List<Orderlist> getOrders() { return orders; }
    public void setOrders(List<Orderlist> orders) { this.orders = orders; }
}
