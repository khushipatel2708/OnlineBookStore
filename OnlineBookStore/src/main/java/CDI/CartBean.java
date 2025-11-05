package CDI;

import EJB.CartSessionLocal;
import Entity.Cart;
import jakarta.ejb.EJB;
import jakarta.enterprise.context.SessionScoped;
import jakarta.inject.Named;
import java.io.Serializable;
import java.util.List;

@Named("cartBean")
@SessionScoped
public class CartBean implements Serializable {

    @EJB
    private CartSessionLocal cartSession;

    private int loggedUserId = 1; // (you can dynamically set this after login)

    public void addToCart(int bookId) {
        cartSession.addToCart(loggedUserId, bookId);
    }

    public List<Cart> getCartItems() {
        return cartSession.getCartItems(loggedUserId);
    }
}
