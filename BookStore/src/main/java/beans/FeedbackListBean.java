package beans;

import EJB.UserSessionBeanLocal;
import Entity.Feedback;
import jakarta.annotation.PostConstruct;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.enterprise.context.RequestScoped;
import java.util.List;

@Named
@RequestScoped
public class FeedbackListBean {

    @Inject
    private UserSessionBeanLocal userEJB;

    private List<Feedback> feedbackList;

    @PostConstruct
    public void init() {
        feedbackList = userEJB.getAllFeedbacks();
    }

    public List<Feedback> getFeedbackList() {
        return feedbackList;
    }
}
