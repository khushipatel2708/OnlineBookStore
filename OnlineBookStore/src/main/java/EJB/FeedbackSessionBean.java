package EJB;

import Entity.FeedBack;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.List;

@Stateless
public class FeedbackSessionBean implements FeedbackSessionBeanLocal {

    @PersistenceContext(unitName = "my_pu")
    private EntityManager em;

    @Override
    public void addFeedback(FeedBack f) {
        em.persist(f);
    }

    @Override
    public void updateFeedback(FeedBack f) {
        em.merge(f);
    }

    @Override
    public void deleteFeedback(int id) {
        FeedBack f = em.find(FeedBack.class, id);
        if (f != null) em.remove(f);
    }

    @Override
    public FeedBack getFeedbackById(int id) {
        return em.find(FeedBack.class, id);
    }

    @Override
    public List<FeedBack> getAllFeedback() {
        return em.createQuery("SELECT f FROM Feedback f", FeedBack.class).getResultList();
    }

    @Override
    public List<FeedBack> getFeedbackByBook(int bookId) {
        return em.createQuery("SELECT f FROM Feedback f WHERE f.book.id = :bid", FeedBack.class)
                 .setParameter("bid", bookId)
                 .getResultList();
    }

    @Override
    public List<FeedBack> getFeedbackByUser(int userId) {
        return em.createQuery("SELECT f FROM Feedback f WHERE f.user.id = :uid", FeedBack.class)
                 .setParameter("uid", userId)
                 .getResultList();
    }
}
