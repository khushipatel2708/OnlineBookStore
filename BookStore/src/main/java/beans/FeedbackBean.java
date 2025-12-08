package beans;

import EJB.UserSessionBeanLocal;
import jakarta.annotation.PostConstruct;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.enterprise.context.RequestScoped;
import jakarta.faces.context.FacesContext;

@Named
@RequestScoped
public class FeedbackBean {

    @Inject
    private UserSessionBeanLocal userEJB;

    @Inject
    private LoginBean loginBean;

    private int bookId;
    private int rating;
    private String comments;

    @PostConstruct
    public void init() {
        String idStr = FacesContext.getCurrentInstance()
                .getExternalContext()
                .getRequestParameterMap()
                .get("bookId");

        if (idStr != null && !idStr.isEmpty()) {
            try {
                bookId = Integer.parseInt(idStr);
            } catch (NumberFormatException e) {
                System.out.println("Invalid bookId: " + idStr);
            }
        } else {
            System.out.println("bookId not found in URL");
        }
    }

    public void loadParams() {
        // receive bookId from URL
        String idStr = FacesContext.getCurrentInstance()
                .getExternalContext()
                .getRequestParameterMap()
                .get("bookId");

        if (idStr != null) {
            bookId = Integer.parseInt(idStr);
        }
    }

    public String submitFeedback() {
        int userId = loginBean.getLoggedInUser().getId();

        userEJB.addFeedback(comments, rating, bookId, userId);

        return "success.jsf?faces-redirect=true";
    }

    // getters & setters
    public int getBookId() {
        return bookId;
    }

    public void setBookId(int bookId) {
        this.bookId = bookId;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }
}