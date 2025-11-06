package CDI;

import EJB.CartSessionLocal;
import Entity.Cart;
import Entity.User;
import jakarta.ejb.EJB;
import jakarta.enterprise.context.SessionScoped;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import java.io.IOException;
import java.io.Serializable;
import java.util.List;

@Named("cartBean")
@SessionScoped
public class CartBean implements Serializable {

    @EJB
    private CartSessionLocal cartSession;

    // ✅ Inject LoginBean to get logged-in user
    @Inject
    private LoginBean loginBean;

    public void addToCart(int bookId) {
        User loggedUser = loginBean.getLoggedInUser();

        if (loggedUser != null) {
            int userId = loggedUser.getId();
            cartSession.addToCart(userId, bookId);
        } else {
            System.out.println("⚠️ No user logged in!");
        }
    }

    public List<Cart> getCartItems() {
        User loggedUser = loginBean.getLoggedInUser();
        if (loggedUser != null) {
            return cartSession.getCartItems(loggedUser.getId());
        }
        return null;
    }

    public void removeFromCart(int cartId) {
        cartSession.removeFromCart(cartId);
    }

    public double getTotalPrice() {
        double total = 0.0;
        List<Cart> items = getCartItems();
        if (items != null) {
            for (Cart c : items) {
                total += c.getBook().getPrice() * c.getQuantity();
            }
        }
        return total;
    }

    public void increaseQuantity(int cartId) {
        cartSession.updateQuantity(cartId, +1);
    }

    public void decreaseQuantity(int cartId) {
        cartSession.updateQuantity(cartId, -1);
    }

    // ✅ Redirect to payment.xhtml with total amount (including delivery)
    public void goToPayment() throws IOException {
        double grandTotal = getTotalPrice() + 30;
        FacesContext.getCurrentInstance().getExternalContext().redirect(
                "payment.xhtml?amount=" + grandTotal);
    }
}
