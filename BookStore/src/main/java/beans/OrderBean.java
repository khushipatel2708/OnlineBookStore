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
 private Orderlist selectedOrder;
    // ---------------- Admin ----------------
    public List<Orderlist> getPendingOrders() {
        return userSessionBean.getPendingOrders();
    }

    public void deliverOrder(Integer orderId) {
        userSessionBean.markAsDelivered(orderId);
    }

    // ---------------- User ----------------
    public List<Orderlist> getUserOrders(User user) {
        return userSessionBean.getOrdersByUser(user);
    }
     // Getter & Setter
    public Orderlist getSelectedOrder() { return selectedOrder; }
    public void setSelectedOrder(Orderlist selectedOrder) { this.selectedOrder = selectedOrder; }

    // Load order by ID
    public void loadOrderById(Integer orderId) {
        selectedOrder = userSessionBean.getOrderById(orderId); // Use EJB method
    }
}
