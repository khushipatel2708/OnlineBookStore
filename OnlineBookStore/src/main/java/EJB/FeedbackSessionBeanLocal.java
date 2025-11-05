package EJB;

import Entity.FeedBack;
import java.util.List;
import jakarta.ejb.Local;

@Local
public interface FeedbackSessionBeanLocal {
    void addFeedback(FeedBack f);
    void updateFeedback(FeedBack f);
    void deleteFeedback(int id);
    FeedBack getFeedbackById(int id);
    List<FeedBack> getAllFeedback();
    List<FeedBack> getFeedbackByBook(int bookId);
    List<FeedBack> getFeedbackByUser(int userId);
}