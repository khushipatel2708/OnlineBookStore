package beans;

import EJB.UserSessionBeanLocal;
import Entity.Cart;
import Entity.User;
import jakarta.enterprise.context.SessionScoped;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;

import jakarta.servlet.http.HttpServletResponse;

@Named
@SessionScoped
public class PaymentBean implements Serializable {

    @Inject
    private UserSessionBeanLocal userEJB;

    @Inject
    private LoginBean loginBean; // Logged-in user

    private List<Cart> cartItems;
    private BigDecimal totalAmount;
    private String phone;
    private String email;
    private final String firstname = "User"; // PayU requirement
    private final String key = "BOQDTs"; // PayU key
    private final String salt = "s1F30l4tJHVz2MCz56FheF3gzqTbozfU"; // PayU salt
    private final String payuURL = "https://test.payu.in/_payment";

    // ---------------- Cart & Total ----------------
    public List<Cart> getCartItems() {
        if (cartItems == null && loginBean.isLoggedIn()) {
            cartItems = userEJB.getCartItems(loginBean.getLoggedInUser().getId());
        }
        return cartItems;
    }

    public BigDecimal getTotalAmount() {
        if (totalAmount == null && getCartItems() != null) {
            totalAmount = getCartItems().stream()
                    .map(c -> c.getBookId().getPrice().multiply(BigDecimal.valueOf(c.getQuantity())))
                    .reduce(BigDecimal.ZERO, BigDecimal::add);

            // ➕ Add delivery charge ₹30
            totalAmount = totalAmount.add(BigDecimal.valueOf(30));
        }
        return totalAmount;
    }

    // ---------------- PayU Payment ----------------
    
    // Getters & Setters
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    
    private String formatAmount(BigDecimal amt) {
        return amt.setScale(1, BigDecimal.ROUND_HALF_UP).toPlainString();
    }

//    private String generateHash(String txnid) {
//        String hashString = key + "|" + txnid + "|" + formatAmount(totalAmount) + "|Book Purchase|"
//                + firstname + "|" + loginBean.getLoggedInUser().getUsername()
//                + "|||||||||||" + salt;
//        return hashCal("SHA-512", hashString);
//    }

    private String generateHash(String txnid) {
        String hashString = key + "|" + txnid + "|" + formatAmount(totalAmount) + "|" + "Book Purchase" + "|" +
                firstname + "|" + email + "|||||||||||" + salt;
        return hashCal("SHA-512", hashString);
    }
    
    private String hashCal(String type, String str) {
        try {
            MessageDigest digest = MessageDigest.getInstance(type);
            byte[] hashbytes = digest.digest(str.getBytes());
            BigInteger number = new BigInteger(1, hashbytes);
            StringBuilder hexString = new StringBuilder(number.toString(16));
            while (hexString.length() < 128) {
                hexString.insert(0, '0');
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    // Called on "Pay Now" button
    public void makePayment() throws IOException {
        User user = loginBean.getLoggedInUser();
        if (user == null) {
            FacesContext.getCurrentInstance().getExternalContext().redirect("login.xhtml");
            return;
        }

        // Save payments in DB as "Pending"
        for (Cart c : getCartItems()) {
            userEJB.addPayment(
                    user,
                    c.getBookId(),
                    "PayU",
                    c.getBookId().getPrice().multiply(BigDecimal.valueOf(c.getQuantity())),
                    phone,
                    "Pending"
            );
        }

        // PayU redirect
        String txnid = "TXN" + System.currentTimeMillis();
        String hash = generateHash(txnid);

        String surl = "https://localhost:8181/BookStore/success.jsf";
        String furl = "https://localhost:8181/BookStore/failure.jsf";

        HttpServletResponse response
                = (HttpServletResponse) FacesContext.getCurrentInstance()
                        .getExternalContext()
                        .getResponse();

        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        out.println("<html><body onload='document.forms[0].submit()'>");
        out.println("<form action='" + payuURL + "' method='post'>");
        out.println("<input type='hidden' name='key' value='" + key + "'/>");
        out.println("<input type='hidden' name='txnid' value='" + txnid + "'/>");
        out.println("<input type='hidden' name='amount' value='" + formatAmount(totalAmount) + "'/>");
        out.println("<input type='hidden' name='productinfo' value='Book Purchase'/>");
        out.println("<input type='hidden' name='firstname' value='" + firstname + "'/>");
        out.println("<input type='hidden' name='email' value='" + email + "'/>");
        out.println("<input type='hidden' name='phone' value='" + phone + "'/>");
        out.println("<input type='hidden' name='surl' value='" + surl + "'/>");
        out.println("<input type='hidden' name='furl' value='" + furl + "'/>");
        out.println("<input type='hidden' name='hash' value='" + hash + "'/>");
        out.println("</form>");
        out.println("<p>Redirecting to PayU secure payment page...</p>");
        out.println("</body></html>");
        out.close();

        FacesContext.getCurrentInstance().responseComplete();
    }

    public void clearCartAfterSuccess() {
        try {
            int userId = loginBean.getLoggedInUser().getId();
            List<Cart> list = userEJB.getCartItems(userId);

            for (Cart c : list) {
                userEJB.removeFromCart(c.getId());   // Remove each item
            }

            cartItems = null; // Reset local cache
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // ---------------- Getters & Setters ----------------
    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
