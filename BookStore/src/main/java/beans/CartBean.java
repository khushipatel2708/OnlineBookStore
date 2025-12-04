package beans;


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

import EJB.UserSessionBeanLocal;
import beans.LoginBean;

@Named("cartBean")
@SessionScoped
public class CartBean implements Serializable {

    @EJB
    private UserSessionBeanLocal cartSession;

    @Inject
    private LoginBean loginBean;

    public void addToCart(int bookId) {
        User logged = loginBean.getLoggedInUser();
        if (logged != null) {
            cartSession.addToCart(logged.getId(), bookId);
        }
    }

    public List<Cart> getCartItems() {
        User logged = loginBean.getLoggedInUser();
        if (logged != null) {
            return cartSession.getCartItems(logged.getId());
        }
        return null;
    }

    public void removeFromCart(int cartId) {
        cartSession.removeFromCart(cartId);
    }

    public void increaseQuantity(int cartId) {
        cartSession.updateQuantity(cartId, +1);
    }

    public void decreaseQuantity(int cartId) {
        cartSession.updateQuantity(cartId, -1);
    }

    // NEW — REQUIRED FOR cartList.xhtml
//    public double getTotalPrice() {
//        double total = 0.0;
//        List<Cart> items = getCartItems();
//        if (items != null) {
//            for (Cart c : items) {
//                total += c.getBookId().getPrice() * c.getQuantity();
//            }
//        }
//        return total;
//    }
}
