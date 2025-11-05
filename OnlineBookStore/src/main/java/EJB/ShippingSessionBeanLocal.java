package EJB;

import Entity.Shipping;
import java.util.List;
import jakarta.ejb.Local;

@Local
public interface ShippingSessionBeanLocal {
    void addShipping(Shipping s);
    void updateShipping(Shipping s);
    void deleteShipping(int id);
    Shipping getShippingById(int id);
    List<Shipping> getShippingByUser(int userid);
}
