package EJB;

import Entity.Payment;
import jakarta.ejb.Local;

@Local
public interface PaymentSessionBeanLocal {
    void addPayment(Payment payment);
    void updatePayment(Payment payment);
    Payment findById(int id);
}
