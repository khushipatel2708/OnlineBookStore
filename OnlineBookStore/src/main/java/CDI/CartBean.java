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

    private int loggedUserId = 1; // assume logged in user id

    public void addToCart(int bookId) {
        cartSession.addToCart(loggedUserId, bookId);
    }

    public List<Cart> getCartItems() {
        return cartSession.getCartItems(loggedUserId);
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


}
