package beans;

import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Named;
import jakarta.inject.Inject;
import jakarta.mail.*;
import jakarta.mail.internet.*;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;

import java.util.Properties;

@Named("contactBean")
@RequestScoped
public class ContactBean {

    private String subject;
    private String message;

    @Inject
    private LoginBean loginBean; // get logged in user email

public String sendMessage() {

    String userEmail = loginBean.getLoggedInUser().getEmail();
    String foundationEmail = "bookingonline40@gmail.com";

    try {
        sendEmail(userEmail, foundationEmail, subject, message);

        // success flag pass karva
        FacesContext.getCurrentInstance()
                .getExternalContext()
                .getFlash()
                .put("contactSuccess", true);

        // same page reload
         return null;

    } catch (Exception e) {
        e.printStackTrace();
        return null;
    }
}

    // ---------------- EMAIL SENDING FUNCTION ------------------
    /**
     * @param senderEmail  -> foundation email (actual sender)
     * @param replyToEmail -> login user's email (reply-to)
     */
   public void sendEmail(String replyToEmail,
                      String toEmail,
                      String subject,
                      String msg) throws Exception {

    Properties props = new Properties();
    props.put("mail.smtp.host", "smtp.gmail.com");
    props.put("mail.smtp.port", "587");
    props.put("mail.smtp.auth", "true");
    props.put("mail.smtp.starttls.enable", "true");

    final String username = "bookingonline40@gmail.com";
    final String password = "bcidtabltwqbweup"; // App password

    Session session = Session.getInstance(props, new Authenticator() {
        protected PasswordAuthentication getPasswordAuthentication() {
            return new PasswordAuthentication(username, password);
        }
    });

    Message messageObj = new MimeMessage(session);

    // Gmail requires FROM = same account
    messageObj.setFrom(new InternetAddress(username));

    // Reply will go to user
    messageObj.setReplyTo(new Address[]{
        new InternetAddress(replyToEmail)
    });

    // Mail will be received here
    messageObj.setRecipients(
        Message.RecipientType.TO,
        InternetAddress.parse(toEmail)
    );

    messageObj.setSubject("Contact Us Form Submission");

   messageObj.setText(
    "Contact Us Form Submission\n"
  + "----------------------------\n"
  + "User Email: " + replyToEmail + "\n\n"
  + "Subject:\n"
  + subject + "\n\n"
  + "Message:\n"
  + msg
);


    Transport.send(messageObj);
}

    // getters & setters
    public String getSubject() { return subject; }
    public void setSubject(String subject) { this.subject = subject; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
}
