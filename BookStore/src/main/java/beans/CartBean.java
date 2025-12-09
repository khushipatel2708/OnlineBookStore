package beans;

import EJB.UserSessionBeanLocal;
import Entity.Cart;
import Entity.User;
import jakarta.ejb.EJB;
import jakarta.enterprise.context.SessionScoped;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import java.io.Serializable;
import java.util.List;
import java.util.Map;

@Named("cartBean")
@SessionScoped
public class CartBean implements Serializable {

    @EJB
    private UserSessionBeanLocal cartSession; // your EJB

    @Inject
    private PaymentBean paymentBean;
    
    @Inject
    private LoginBean loginBean; // must provide getLoggedInUser() or getUserid()

    // --- Add to cart (reads f:param bookId) ---
    public String addToCart() {
        FacesContext fc = FacesContext.getCurrentInstance();
        Map<String, String> params = fc.getExternalContext().getRequestParameterMap();
        String bookIdStr = params.get("bookId");

        if (bookIdStr == null || bookIdStr.trim().isEmpty()) {
            fc.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "No book selected.", null));
            return null;
        }

        try {
            int bookId = Integer.parseInt(bookIdStr);

            // Get logged-in user either by userid or User object from LoginBean
            User loggedUser = loginBean.getLoggedInUser(); // prefer this method
            Integer userId = null;
            if (loggedUser != null) {
                userId = loggedUser.getId();
            } else if (loginBean.getUserid() != null) { // fallback to userid field if you have it
                userId = loginBean.getUserid();
            }

            if (userId == null) {
                // not logged in — redirect to login (or show message)
                fc.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Please login to add to cart.", null));
                // optional: redirect
                // fc.getExternalContext().redirect("login.xhtml");
                return null;
            }

            // call EJB to add
            cartSession.addToCart(userId, bookId);

            fc.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Added to cart.", null));
        } catch (NumberFormatException ex) {
            fc.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Invalid book id.", null));
        } catch (Exception ex) {
            fc.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Could not add to cart: " + ex.getMessage(), null));
            ex.printStackTrace();
        }
        return null; // stay on same page
    }

    // --- helper to return cart items if you need it in view ---
    public List<Cart> getCartItems() {
        User loggedUser = loginBean.getLoggedInUser();
        if (loggedUser != null) {
            return cartSession.getCartItems(loggedUser.getId());
        }
        return null;
    }

    // remove / update methods (optional)
    public void removeFromCart(int cartId) {
        cartSession.removeFromCart(cartId);
    }
    
    public void deleteItem(int id) {
     cartSession.removeFromCart(id);  // your delete logic
}

    

    public void increaseQuantity(int cartId) {
        Cart c = cartSession.getCartById(cartId); // you need a method to get cart by id
        if (c != null && c.getQuantity() < c.getBookId().getAvailable()) {
            cartSession.updateQuantity(cartId, +1);
        } else {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_WARN, "Cannot exceed available stock!", null));
        }
    }

    public void decreaseQuantity(int cartId) {
        Cart c = cartSession.getCartById(cartId);
        if (c != null && c.getQuantity() > 1) {
            cartSession.updateQuantity(cartId, -1);
        }
    }

    public int getCartCount() {
        User loggedUser = loginBean.getLoggedInUser();

        if (loggedUser != null) {
            List<Cart> list = cartSession.getCartItems(loggedUser.getId());
            return list != null ? list.size() : 0;
        }

        return 0;
    }

    public double getTotalPrice() {
        User loggedUser = loginBean.getLoggedInUser();
        if (loggedUser != null) {
            List<Cart> cart = cartSession.getCartItems(loggedUser.getId());
            java.math.BigDecimal total = java.math.BigDecimal.ZERO;

            if (cart != null) {
                for (Cart c : cart) {
                    java.math.BigDecimal price = c.getBookId().getPrice();
                    int qty = c.getQuantity();
                    total = total.add(price.multiply(java.math.BigDecimal.valueOf(qty)));
                }
            }
            return total.doubleValue();
        }
        return 0;
    }

    public double getFinalTotal() {
        return getTotalPrice() + 30;
    }

    public String goToPayment() {
        return "payment.xhtml?faces-redirect=true";
    }

    public String cashOnDelivery() {
        try {
            User loggedUser = loginBean.getLoggedInUser();
            if (loggedUser == null) {
                FacesContext.getCurrentInstance()
                        .addMessage(null, new FacesMessage("Please login first."));
                return null;
            }

            Integer userId = loggedUser.getId();
            List<Cart> items = cartSession.getCartItems(userId);

            if (items == null || items.isEmpty()) {
                FacesContext.getCurrentInstance()
                        .addMessage(null, new FacesMessage("Cart is empty."));
                return null;
            }

            // Loop through cart items and create COD payment for each
            for (Cart c : items) {
                
                 paymentBean.setLastBookId(c.getBookId().getId());
                 
                java.math.BigDecimal price = c.getBookId().getPrice();
                java.math.BigDecimal qty = java.math.BigDecimal.valueOf(c.getQuantity());
                java.math.BigDecimal amount = price.multiply(qty);

                // 1️⃣ Save COD payment
                cartSession.addCODPayment(userId, c.getBookId().getId(), amount);

                // 2️⃣ Update Book stock
                int newStock = c.getBookId().getAvailable() - c.getQuantity();
                if (newStock < 0) {
                    newStock = 0; // prevent negative stock
                }
                cartSession.updateBookStock(c.getBookId().getId(), newStock);

                // 3️⃣ Remove from cart
                cartSession.removeFromCart(c.getId());
            }

            return "success.xhtml?faces-redirect=true";

        } catch (Exception ex) {
            ex.printStackTrace();
            FacesContext.getCurrentInstance()
                    .addMessage(null, new FacesMessage("COD failed: " + ex.getMessage()));
            return null;
        }
    }
    

}
