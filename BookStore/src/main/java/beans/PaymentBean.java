package beans;

import EJB.UserSessionBeanLocal;
import Entity.Cart;
import Entity.Payment;
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
    private LoginBean loginBean;

    private List<Cart> cartItems;
    private BigDecimal totalAmount;

    private String phone;
    private String email;

    private int lastBookId;

    private final String firstname = "User";

    private final String key = "BOQDTs";
    private final String salt = "s1F30l4tJHVz2MCz56FheF3gzqTbozfU";

    private final String payuURL = "https://test.payu.in/_payment";

    // ----------------- Cart Items -----------------
    public List<Cart> getCartItems() {
        if (cartItems == null && loginBean.isLoggedIn()) {
            cartItems = userEJB.getCartItems(loginBean.getLoggedInUser().getId());
        }
        return cartItems;
    }

    // ----------------- Total Amount -----------------
    public BigDecimal getTotalAmount() {
        if (totalAmount == null && getCartItems() != null) {

            totalAmount = getCartItems()
                    .stream()
                    .map(c -> c.getBookId().getPrice().multiply(new BigDecimal(c.getQuantity())))
                    .reduce(BigDecimal.ZERO, BigDecimal::add);

            // Add ₹30 delivery charge
            totalAmount = totalAmount.add(BigDecimal.valueOf(30));
        }
        return totalAmount;
    }

    private String formatAmount(BigDecimal amt) {
        return amt.setScale(1, BigDecimal.ROUND_HALF_UP).toPlainString();
    }

    // ----------------- HASH GENERATION -----------------
    private String generateHash(String txnid) {

        String hashString = key + "|" + txnid + "|"
                + formatAmount(totalAmount) + "|"
                + "Book Purchase" + "|"
                + firstname + "|"
                + email + "|||||||||||" + salt;

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

    // ----------------- MAKE PAYMENT (MAIN LOGIC) -----------------
    public void makePayment() throws IOException {

        User user = loginBean.getLoggedInUser();
        if (user == null) {
            FacesContext.getCurrentInstance().getExternalContext().redirect("login.xhtml");
            return;
        }

        // 1️⃣ Save Payment → Update Stock → Remove Cart
        for (Cart c : getCartItems()) {
            int bookId = c.getBookId().getId();
            BigDecimal price = c.getBookId().getPrice();
            BigDecimal qty = new BigDecimal(c.getQuantity());
            BigDecimal amount = price.multiply(qty);

            // Save Payment as Pending
            userEJB.addPayment(
                    user,
                    c.getBookId(),
                    "Online Payment",
                    amount,
                    phone,
                    "Pending"
            );

            lastBookId = bookId;

            // Update Stock
            int newStock = c.getBookId().getAvailable() - c.getQuantity();
            if (newStock < 0) {
                newStock = 0;
            }

            userEJB.updateBookStock(c.getBookId().getId(), newStock);

            // Remove Cart Item
            userEJB.removeFromCart(c.getId());
        }

        // 2️⃣ Generate PayU transaction
        String txnid = "TXN" + System.currentTimeMillis();
        String surl = "https://localhost:8181/BookStore/success.jsf";
        String furl = "https://localhost:8181/BookStore/failure.jsf";

        String hash = generateHash(txnid);

        // 3️⃣ Redirect to PayU with auto-submit form
        HttpServletResponse response = (HttpServletResponse) FacesContext.getCurrentInstance()
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
        out.println("<p>Redirecting to PayU...</p>");
        out.println("</body></html>");

        out.close();
        FacesContext.getCurrentInstance().responseComplete();
    }

    // ----------------- Clear Cart After Payment Success -----------------
    public void clearCartAfterSuccess() {

        try {
            int userId = loginBean.getLoggedInUser().getId();
            List<Cart> list = userEJB.getCartItems(userId);

            for (Cart c : list) {
                userEJB.removeFromCart(c.getId());
            }

            cartItems = null;

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<Payment> getAllPayments() {

        List<Payment> list = userEJB.getAllPayments();

        // Search by phone
        if (searchPhone != null && !searchPhone.trim().isEmpty()) {
            list = list.stream()
                    .filter(p -> p.getPhone() != null
                    && p.getPhone().toLowerCase().contains(searchPhone.toLowerCase()))
                    .toList();
        }

        // Filter by payment method
        if (searchMethod != null && !searchMethod.equals("All") && !searchMethod.isEmpty()) {
            list = list.stream()
                    .filter(p -> p.getPaymentMethod().equalsIgnoreCase(searchMethod))
                    .toList();
        }

        // 🔥 Search by Username
        if (searchUser != null && !searchUser.trim().isEmpty()) {
            list = list.stream()
                    .filter(p -> p.getUserId() != null
                    && p.getUserId().getFullname().toLowerCase().contains(searchUser.toLowerCase()))
                    .toList();
        }

        // 🔥 Search by Book Name
        if (searchBook != null && !searchBook.trim().isEmpty()) {
            list = list.stream()
                    .filter(p -> p.getBookId() != null
                    && p.getBookId().getBookname().toLowerCase().contains(searchBook.toLowerCase()))
                    .toList();
        }

        return list;
    }

    // Method to calculate total amount with +30 for each payment
    public BigDecimal getListTotalAmount() {
        BigDecimal total = BigDecimal.ZERO;
        for (Payment p : getAllPayments()) {
            if (p.getAmount() != null) {
                total = total.add(p.getAmount().add(BigDecimal.valueOf(30))); // add 30 to each amount
            } else {
                total = total.add(BigDecimal.valueOf(30)); // if amount null, still add 30
            }
        }
        return total;
    }

    // ----------------- Getters & Setters -----------------
    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getLastBookId() {
        return lastBookId;
    }

    public void setLastBookId(int lastBookId) {
        this.lastBookId = lastBookId;
    }

    private int pageSize = 5;      // rows per page
    private int pageNumber = 1;

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getPageNumber() {
        return pageNumber;
    }

    public void setPageNumber(int pageNumber) {
        this.pageNumber = pageNumber;
    }

    public int getTotalPages() {
        int total = getAllPayments().size();
        return (int) Math.ceil((double) total / pageSize);
    }

    public List<Payment> getPagedPayments() {

        List<Payment> all = getAllPayments();

        int fromIndex = (pageNumber - 1) * pageSize;
        int toIndex = Math.min(fromIndex + pageSize, all.size());

        if (fromIndex > all.size()) {
            return java.util.Collections.emptyList();
        }

        return all.subList(fromIndex, toIndex);
    }

    public void nextPage() {
        if (pageNumber < getTotalPages()) {
            pageNumber++;
        }
    }

    public void previousPage() {
        if (pageNumber > 1) {
            pageNumber--;
        }
    }

    //Search
    private String searchPhone = "";
    private String searchMethod = "";

    public String getSearchPhone() {
        return searchPhone;
    }

    public void setSearchPhone(String searchPhone) {
        this.searchPhone = searchPhone;
    }

    public String getSearchMethod() {
        return searchMethod;
    }

    public void setSearchMethod(String searchMethod) {
        this.searchMethod = searchMethod;
    }

    public void searchPayments() {
        pageNumber = 1; // reset pagination
    }

    private String searchUser = "";
    private String searchBook = "";

    public String getSearchUser() {
        return searchUser;
    }

    public void setSearchUser(String searchUser) {
        this.searchUser = searchUser;
    }

    public String getSearchBook() {
        return searchBook;
    }

    public void setSearchBook(String searchBook) {
        this.searchBook = searchBook;
    }

    public String clearSearch() {
        searchPhone = "";
        searchMethod = "All"; // default value
        searchUser = "";
        searchBook = "";
        pageNumber = 1;       // reset pagination
        return null;           // stay on same page
    }

}
