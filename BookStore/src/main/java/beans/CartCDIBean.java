package beans;

import Entity.Cart;
import Entity.User;
import Entity.Book;
import client.UserClient;
import jakarta.inject.Named;
import jakarta.enterprise.context.SessionScoped;
import jakarta.inject.Inject;
import java.io.Serializable;
import java.util.Collection;

@Named("cartBean")
@SessionScoped
public class CartCDIBean implements Serializable {

    @Inject
    private LoginBean loginBean;

    private Collection<Cart> cartItems;

    private UserClient userClient = new UserClient();

    // ============ ADD TO CART ============
    public void addToCart(Integer bookId) {
        try {
            Integer userId = loginBean.getUserid();

            Cart c = new Cart();
            User u = new User();
            u.setId(userId);

            Book b = new Book();
            b.setId(bookId);

            c.setUserId(u);
            c.setBookId(b);
            c.setQuantity(1);

            userClient.addToCart(c);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // ============ LOAD CART ITEMS ============
    public void loadCart() {
        Integer userId = loginBean.getUserid();
        Cart carts = userClient.getCartByUser(Cart.class, userId.toString());
        cartItems = java.util.Arrays.asList(carts);
    }

    public Collection<Cart> getCartItems() {
        return cartItems;
    }
    // ============ GO TO CART PAGE ============
    public String goToCart() {
        loadCart();
        return "Cart.xhtml?faces-redirect=true";
    }

    // ========= DELETE CART ITEM ==========
    public void deleteItem(Integer cartId) {
        userClient.deleteCartItem(cartId.toString());
        loadCart();
    }

    // ========= UPDATE QUANTITY ==========
    public void updateQuantity(Integer cartId, Integer q) {
        Cart c = new Cart();
        c.setQuantity(q);

        userClient.updateCart(c, cartId.toString());
        loadCart();
    }

}
