package CDI;

import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Named;
import jakarta.mail.*;
import jakarta.mail.internet.*;
import java.util.Properties;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;

@Named(value = "contactBean")
@RequestScoped
public class ContactBean {

    private String email;
    private String subject;
    private String description;

    // Getters and setters
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public String getSubject() {
        return subject;
    }
    public void setSubject(String subject) {
        this.subject = subject;
    }
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }

    // ✅ Method to send email
    public void sendEmail() {
        final String toEmail = "bookingonline40@gmail.com";  // Admin email
        final String fromEmail = "pkhush818@gmail.com";       // Replace with your Gmail
        final String password = "bcidtabltwqbweup";          // Use App Password (not Gmail login password)

        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.gmail.com"); // SMTP Host
        props.put("mail.smtp.port", "587"); // TLS Port
        props.put("mail.smtp.auth", "true"); // Enable authentication
        props.put("mail.smtp.starttls.enable", "true"); // Enable STARTTLS

        Session session = Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(fromEmail, password);
            }
        });

        try {
            Message msg = new MimeMessage(session);
            msg.setFrom(new InternetAddress(fromEmail));
            msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail));
            msg.setSubject("Contact from: " + email + " | " + subject);
            msg.setText("Sender Email: " + email + "\n\nMessage:\n" + description);

            Transport.send(msg);

            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO,
                            "Message Sent Successfully!", "Thank you for contacting us."));

            // Clear form after sending
            email = "";
            subject = "";
            description = "";

        } catch (MessagingException e) {
            e.printStackTrace();
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR,
                            "Error!", "Unable to send message."));
        }
    }
}
