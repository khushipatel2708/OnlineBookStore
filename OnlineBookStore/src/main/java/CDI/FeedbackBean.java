package CDI;

import EJB.FeedbackSessionBeanLocal;
import Entity.FeedBack;
import Entity.Book;
import Entity.User;
import jakarta.ejb.EJB;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import java.io.Serializable;
import java.util.List;
import java.util.Map;

@Named(value = "feedbackBean")
@RequestScoped
public class FeedbackBean implements Serializable {

    @EJB
    private FeedbackSessionBeanLocal feedbackSessionBean;

    @Inject
    private LoginBean loginBean; // to get logged-in user

    private FeedBack feedback = new FeedBack();
    private Integer bookId;
    private Integer userId;

    // open form with bookId (userId optional)
    public String openFeedbackForm(Integer bookId) {
        User u = loginBean.getLoggedInUser();
        this.bookId = bookId;
        this.userId = (u != null) ? u.getId() : null;
        // redirect to feedback.xhtml with query params
        String url = "feedback.xhtml?faces-redirect=true&bookId=" + bookId;
        if (this.userId != null) url += "&userId=" + this.userId;
        return url;
    }

    // called on page load to pick up URL params
    public void onLoad() {
        Map<String, String> params = FacesContext.getCurrentInstance()
                .getExternalContext().getRequestParameterMap();
        if (params.get("bookId") != null) {
            this.bookId = Integer.valueOf(params.get("bookId"));
            Book b = new Book(); b.setId(this.bookId); feedback.setBook(b);
        }
        if (params.get("userId") != null) {
            this.userId = Integer.valueOf(params.get("userId"));
            User u = new User(); u.setId(this.userId); feedback.setUser(u);
        } else {
            User u = loginBean.getLoggedInUser();
            if (u != null) { feedback.setUser(u); this.userId = u.getId(); }
        }
    }

    public String saveFeedback() {
        try {
            // ensure book/user relation objects exist
            if (feedback.getBook() == null && bookId != null) {
                Book b = new Book(); b.setId(bookId); feedback.setBook(b);
            }
            if (feedback.getUser() == null && userId != null) {
                User u = new User(); u.setId(userId); feedback.setUser(u);
            }

            // basic validation
            if (feedback.getRating() == null || feedback.getRating() < 1 || feedback.getRating() > 5) {
                FacesContext.getCurrentInstance().addMessage(null,
                        new FacesMessage(FacesMessage.SEVERITY_ERROR, "Rating must be 1 to 5", null));
                return null;
            }

            if (feedback.getId() == null) feedbackSessionBean.addFeedback(feedback);
            else feedbackSessionBean.updateFeedback(feedback);

            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage("Feedback saved successfully."));
            // go back to book list or book details
            return "userBookList.xhtml?faces-redirect=true";
        } catch (Exception e) {
            e.printStackTrace();
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error: " + e.getMessage(), null));
            return null;
        }
    }

    public String editFeedback(int id) {
        FeedBack f = feedbackSessionBean.getFeedbackById(id);
        if (f != null) {
            this.feedback = f;
            this.bookId = (f.getBook() != null) ? f.getBook().getId() : null;
            this.userId = (f.getUser() != null) ? f.getUser().getId() : null;
        }
        return "feedback.xhtml?faces-redirect=true&bookId=" + (bookId!=null?bookId:"");
    }

    public void deleteFeedback(int id) {
        feedbackSessionBean.deleteFeedback(id);
        FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage("Feedback deleted."));
    }

    // list helpers
    public List<FeedBack> getAllFeedback() { return feedbackSessionBean.getAllFeedback(); }
    public List<FeedBack> getFeedbackByBook(int bookId) { return feedbackSessionBean.getFeedbackByBook(bookId); }
    public List<FeedBack> getFeedbackByUser(int userId) { return feedbackSessionBean.getFeedbackByUser(userId); }

    // getters & setters
    public FeedBack getFeedback() { return feedback; }
    public void setFeedback(FeedBack feedback) { this.feedback = feedback; }

    public Integer getBookId() { return bookId; }
    public void setBookId(Integer bookId) { this.bookId = bookId; }

    public Integer getUserId() { return userId; }
    public void setUserId(Integer userId) { this.userId = userId; }
}