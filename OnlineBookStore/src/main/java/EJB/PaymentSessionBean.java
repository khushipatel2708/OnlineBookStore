package EJB;

import Entity.Payment;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

@Stateless
public class PaymentSessionBean implements PaymentSessionBeanLocal {

    @PersistenceContext(unitName = "my_pu")
    private EntityManager em;

    @Override
    public void addPayment(Payment payment) {
        em.persist(payment);
    }

    @Override
    public void updatePayment(Payment payment) {
        em.merge(payment);
    }

    @Override
    public Payment findById(int id) {
        return em.find(Payment.class, id);
    }
}
