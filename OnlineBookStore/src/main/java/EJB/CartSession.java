package EJB;

import Entity.Book;
import Entity.Cart;
import Entity.User;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.List;

@Stateless
public class CartSession implements CartSessionLocal {

    @PersistenceContext(unitName = "my_pu")
    private EntityManager em;

    @Override
    public void addToCart(int userId, int bookId) {
        User user = em.find(User.class, userId);
        Book book = em.find(Book.class, bookId);

        if (user != null && book != null) {
            // Check if this book is already in the user's cart
            List<Cart> existing = em.createQuery(
                "SELECT c FROM Cart c WHERE c.user.id = :uid AND c.book.id = :bid", Cart.class)
                .setParameter("uid", userId)
                .setParameter("bid", bookId)
                .getResultList();

            if (!existing.isEmpty()) {
                // Increase quantity if already exists
                Cart cart = existing.get(0);
                cart.setQuantity(cart.getQuantity() + 1);
                em.merge(cart);
            } else {
                // Otherwise, create new cart entry
                Cart cart = new Cart();
                cart.setUser(user);
                cart.setBook(book);
                cart.setQuantity(1);
                em.persist(cart);
            }
        }
    }

    @Override
    public List<Cart> getCartItems(int userId) {
        return em.createQuery("SELECT c FROM Cart c WHERE c.user.id = :uid", Cart.class)
                .setParameter("uid", userId)
                .getResultList();
    }
    
@Override
public void removeFromCart(int cartId) {
    Cart cart = em.find(Cart.class, cartId);
    if (cart != null) {
        em.remove(cart);
    }
}


public void updateQuantity(int cartId, int change) {
    Cart cart = em.find(Cart.class, cartId);
    if (cart != null) {
        int newQty = cart.getQuantity() + change;
        if (newQty <= 0) {
            em.remove(cart); // remove if quantity becomes 0
        } else {
            cart.setQuantity(newQty);
            em.merge(cart);
        }
    }
}


}
