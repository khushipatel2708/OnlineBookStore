package CDI;

import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Named;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import jakarta.faces.context.FacesContext;
import java.io.PrintWriter;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Map;

@Named("paymentBean")
@RequestScoped
public class PaymentBean {
    private String firstname;
    private String email;
    private String phone;
    private String amount;

    private final String key = "BIyoWM";
    private final String salt = "dj7zlIsUAs9YjjlJ8HCX3bVF6V4KtS9F";
    private final String payuURL = "https://test.payu.in/_payment";

    public PaymentBean() {
        // ✅ Automatically get amount from query string
        Map<String, String> params = FacesContext.getCurrentInstance()
                .getExternalContext().getRequestParameterMap();
        this.amount = formatAmount(params.get("amount"));
    }

    // Getters & Setters
    public String getFirstname() { return firstname; }
    public void setFirstname(String firstname) { this.firstname = firstname; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getAmount() { return amount; }
    public void setAmount(String amount) { this.amount = formatAmount(amount); }

    // ✅ Always keep exactly one decimal digit (e.g., 10.0)
    private String formatAmount(String amt) {
        if (amt == null || amt.trim().isEmpty()) return "0.0";
        try {
            BigDecimal bd = new BigDecimal(amt.trim());
            bd = bd.setScale(1, BigDecimal.ROUND_HALF_UP);
            return bd.toPlainString(); // ensures 10.0 instead of 1E+1
        } catch (NumberFormatException e) {
            return "0.0";
        }
    }

    private String generateHash(String txnid) {
        String hashString = key + "|" + txnid + "|" + amount + "|" + "Book Purchase" + "|" +
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

    public void makePayment() throws IOException {
        String txnid = "TXN" + System.currentTimeMillis();
        String hash = generateHash(txnid);

        String surl = "http://localhost:8080/OnlineBookStore/success.xhtml";
        String furl = "http://localhost:8080/OnlineBookStore/failure.xhtml";

        HttpServletResponse response =
            (HttpServletResponse) FacesContext.getCurrentInstance()
                                              .getExternalContext()
                                              .getResponse();

        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        out.println("<html><body onload='document.forms[0].submit()'>");
        out.println("<form action='" + payuURL + "' method='post'>");
        out.println("<input type='hidden' name='key' value='" + key + "'/>");
        out.println("<input type='hidden' name='txnid' value='" + txnid + "'/>");
        out.println("<input type='hidden' name='amount' value='" + amount + "'/>");
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
}
