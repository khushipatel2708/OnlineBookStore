package beans;

import EJB.UserSessionBeanLocal;
import Entity.Orderlist;
import Entity.User;
import jakarta.ejb.EJB;
import jakarta.enterprise.context.SessionScoped;
import jakarta.inject.Named;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
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
    
     private int pageSize = 3;      // Number of rows per page
    private int pageNumber = 1;    // Current page

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getPageNumber() {
        return pageNumber;
    }

    public void setPageNumber(int pageNumber) {
        this.pageNumber = pageNumber;
    }

    public int getTotalPages() {
        int totalGroups = getPendingOrders().size();
        return (int) Math.ceil((double) totalGroups / pageSize);
    }

    public Collection<Orderlist> getPagedGroups() {
        List<Orderlist> all = new ArrayList<>(getPendingOrders());
        int fromIndex = (pageNumber - 1) * pageSize;
        int toIndex = Math.min(fromIndex + pageSize, all.size());
        if (fromIndex > all.size()) {
            return Collections.emptyList();
        }
        return all.subList(fromIndex, toIndex);
    }

    public void nextPage() {
        if (pageNumber < getTotalPages()) {
            pageNumber++;
        }
    }

    public void previousPage() {
        if (pageNumber > 1) {
            pageNumber--;
        }
    }

}
