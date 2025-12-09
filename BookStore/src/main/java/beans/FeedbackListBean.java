package beans;

import EJB.UserSessionBeanLocal;
import Entity.Feedback;
import jakarta.annotation.PostConstruct;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.enterprise.context.SessionScoped;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Named("feedbackListBean")
@SessionScoped
public class FeedbackListBean implements Serializable {

    @Inject
    private UserSessionBeanLocal userEJB;

    private List<Feedback> allFeedbacks;

    private int pageSize = 5;     // rows per page
    private int pageNumber = 1;   // current page

    @PostConstruct
    public void init() {
        allFeedbacks = userEJB.getAllFeedbacks();
    }

    public List<Feedback> getPagedFeedbacks() {
        List<Feedback> list = new ArrayList<>(allFeedbacks);

        int from = (pageNumber - 1) * pageSize;
        int to = Math.min(from + pageSize, list.size());

        if (from > list.size()) {
            return Collections.emptyList();
        }
        return list.subList(from, to);
    }

    public int getTotalPages() {
        return (int) Math.ceil((double) allFeedbacks.size() / pageSize);
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

    public int getPageNumber() {
        return pageNumber;
    }
    
    public int getPageSize() {
        return pageSize;
    }
}
