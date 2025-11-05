package EJB;

import Entity.Cart;
import java.util.List;
import jakarta.ejb.Local;

@Local
public interface CartSessionLocal {
    void addToCart(int userId, int bookId);
    List<Cart> getCartItems(int userId);
}
