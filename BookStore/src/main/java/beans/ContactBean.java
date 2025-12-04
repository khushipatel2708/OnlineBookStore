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

        String userEmail = loginBean.getLoggedInUser().getEmail();  // login user's email
        String foundationEmail = "bookingonline40@gmail.com";       // main email (sender)

        try {
            sendEmail(foundationEmail, userEmail, foundationEmail, subject, message);

            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO,
                            "Message sent successfully!", null));

        } catch (Exception e) {
            e.printStackTrace();
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR,
                            "Failed to send message.", null));
        }

        return null;
    }

    // ---------------- EMAIL SENDING FUNCTION ------------------
    /**
     * @param senderEmail  -> foundation email (actual sender)
     * @param replyToEmail -> login user's email (reply-to)
     */
    public void sendEmail(String senderEmail, String replyToEmail,
                          String toEmail, String subject, String msg) throws Exception {

        // Mail server properties
        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");

        // Foundation Gmail credentials
        final String username = "bookingonline40@gmail.com"; // your Gmail
        final String password = "bcidtabltwqbweup";        // Gmail App Password

        Session session = Session.getInstance(props, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });

        Message messageObj = new MimeMessage(session);
        messageObj.setFrom(new InternetAddress(senderEmail)); // foundation email
        messageObj.setReplyTo(new Address[]{new InternetAddress(replyToEmail)}); // user email
        messageObj.setRecipients(Message.RecipientType.TO,
                InternetAddress.parse(toEmail));
        messageObj.setSubject(subject);
        messageObj.setText(msg);

        Transport.send(messageObj);
    }

    // getters & setters
    public String getSubject() { return subject; }
    public void setSubject(String subject) { this.subject = subject; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
}
