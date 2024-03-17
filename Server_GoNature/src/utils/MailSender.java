package utils;

import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class MailSender {
	/**
	 * The method connects to the sender email account and sends a message
	 * @param recepient the mail of the receiver (to)
	 * @param title the email title
	 * @param content the email content
	 * @throws Exception if it can't connect to the email account or can't send the message
	 */
	public static void sendMail(String recepient, String title, String content) throws Exception {
		Properties properties = new Properties();
		properties.put("mail.smtp.auth", "true");
		properties.put("mail.smtp.starttls.enable", "true");
		properties.put("mail.smtp.host", "smtp.gmail.com");
		properties.put("mail.smtp.port", "587");
		
		
		String myAccountEmail = "group17gonature@gmail.com";
		String password = "group17gonature!@#";
		
		Session session = Session.getInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(myAccountEmail, password);
            }
        });
		
		Message message = prepareMessage(session, myAccountEmail, recepient, title, content);
		
		Transport.send(message);

        System.out.println("Message sent successfully");
	}
	
	
	/**
	 * The method prepare the message for sending
	 * @param session the session of logging into the sender email account
	 * @param myAccountEmail the mail of the sender (from)
	 * @param recepient the mail of the receiver (to)
	 * @param title the email title
	 * @param content the email content
	 * @return ready message for sending
	 */
	private static Message prepareMessage(Session session, String myAccountEmail, String recepient, String title, String content) {
        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(myAccountEmail));
            message.setRecipient(Message.RecipientType.TO, new InternetAddress(recepient));
            message.setSubject(title);
            //String htmlCode = "<h1> WE LOVE JAVA </h1> <br/> <h2><b>Next Line </b></h2>";
            //message.setContent(htmlCode, "text/html");
            message.setText(content);
            return message;
        } catch (Exception ex) {
            Logger.getLogger(MailSender.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
}
