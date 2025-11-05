package EJB;

import Entity.Shipping;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.List;

@Stateless
public class ShippingSessionBean implements ShippingSessionBeanLocal {

    @PersistenceContext(unitName = "my_pu")
    EntityManager em;

    @Override
    public void addShipping(Shipping s) {
        em.persist(s);
    }

    @Override
    public void updateShipping(Shipping s) {
        em.merge(s);
    }

    @Override
    public void deleteShipping(int id) {
        Shipping s = em.find(Shipping.class, id);
        if (s != null) em.remove(s);
    }

    @Override
    public Shipping getShippingById(int id) {
        return em.find(Shipping.class, id);
    }

    @Override
    public List<Shipping> getShippingByUser(int userid) {
        return em.createQuery("SELECT s FROM Shipping s WHERE s.userid = :uid", Shipping.class)
                 .setParameter("uid", userid)
                 .getResultList();
    }
}

