package com.example.trokyy.tools;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.util.Properties;
public class EmailService {
    private final String senderEmail;
    private final String password;

    public EmailService(String senderEmail, String password) {
        this.senderEmail = senderEmail;
        this.password = password;
    }

    public static void sendEmail(String recipientEmail) {
        String senderEmail = "batoutbata5@gmail.com";
        if (recipientEmail == null || recipientEmail.isEmpty()) {
            System.out.println("Recipient email is null or empty. Cannot send email.");
            return;
        }
        String password = "prxidlislxcnfedc";
        Properties properties = new Properties();
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.smtp.host", "smtp.gmail.com");
        properties.put("mail.smtp.port", "587");
        properties.put("mail.smtp.ssl.trust", "*");
        Session session = Session.getInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(senderEmail, password);
            }
        });


        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(senderEmail));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipientEmail));
            message.setSubject("Welcome Trockino !");
            // Create a MimeMultipart object to hold the email content
            MimeMultipart multipart = new MimeMultipart();

            // Create and add a MimeBodyPart for the text content
            MimeBodyPart textPart = new MimeBodyPart();
            String contactInfo =  "<html>" +
                    "<body>" +
                    "<p style=\"font-family: Arial, sans-serif;\"><font color=\"#006400\">Dear Trockino,</font></p>" +
                    "<p style=\"font-family: Arial, sans-serif;\"><font color=\"#000000\">We are thrilled to welcome you to OUR TROCKY, the platform where sustainability meets convenience!</font></p>" +
                    "<p style=\"font-family: Arial, sans-serif;\"><font color=\"#000000\">At TROCKY, we're committed to making a positive impact on the environment while offering you an innovative way to trade and save. Our mission is simple: to protect the environment and promote sustainability by encouraging the trading of offers and the planting of trees.</font></p>" +
                    "<p style=\"font-family: Arial, sans-serif;\"><font color=\"#000000\">By trading offers on our platform, you not only get great deals but also earn points that can be used to plant trees. Every offer you trade contributes to our collective effort to create a greener and more sustainable future.</font></p>" +
                    "<p style=\"font-family: Arial, sans-serif;\"><font color=\"#000000\">As the CEO of TROCKY, I assure you that your participation in our community is valued and appreciated. Together, we can make a difference!</font></p>" +
                    "<p style=\"font-family: Arial, sans-serif;\"><font color=\"#000000\">If you have any questions or need assistance, please don't hesitate to reach out to our support team at <a href=\"mailto:trokino@gmail.com\">trokino@gmail.com</a> or call us at 50 794 341. We're here to ensure your experience with us is nothing short of exceptional.</font></p>" +
                    "<p style=\"font-family: Arial, sans-serif;\"><font color=\"#000000\">Thank you for joining us on this journey towards a greener planet.</font></p>" +
                    "<p style=\"font-family: Arial, sans-serif;\"><font color=\"#000000\">Best Regards,</font></p>" +
                    "<p style=\"font-family: Arial, sans-serif;\"><b><font color=\"#006400\">Oumayma AMARA</font></b></p>" +
                    "<p style=\"font-family: Arial, sans-serif;\"><b><font color=\"#006400\">CEO of TROCKY</font></b></p>" +
                    "<p style=\"font-family: Arial, sans-serif;\"><a href=\"mailto:trokino@gmail.com\">troky@gmail.com</a> |<b><font color=\"#007bff\">50 794 341</font></b></p>" +
                    "</body>" +
                    "</html>";
            textPart.setContent(contactInfo, "text/html");
            multipart.addBodyPart(textPart);
            message.setContent(multipart);
            session.setDebug(true);
            Transport.send(message);
            System.out.println("Email sent successfully!");
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }


    public static void sendBanEmail(String recipientEmail, String bannedUserName, String banReason) {
        String senderEmail = "batoutbata5@gmail.com";
        if (recipientEmail == null || recipientEmail.isEmpty()) {
            System.out.println("Recipient email is null or empty. Cannot send email.");
            return;
        }
        String password = "prxidlislxcnfedc";
        Properties properties = new Properties();
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.smtp.host", "smtp.gmail.com");
        properties.put("mail.smtp.port", "587");
        properties.put("mail.smtp.ssl.trust", "*");
        Session session = Session.getInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(senderEmail, password);
            }
        });

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(senderEmail));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipientEmail));
            message.setSubject("Account Ban Notification");
            MimeMultipart multipart = new MimeMultipart();
            MimeBodyPart textPart = new MimeBodyPart();
            String banMessage = "<html>" +
                    "<body>" +
                    "<p style=\"font-family: Arial, sans-serif;\"><font color=\"#FF0000\">Dear " + bannedUserName + ",</font></p>" +
                    "<p style=\"font-family: Arial, sans-serif;\"><font color=\"#000000\">We regret to inform you that your account has been banned due to the following reason:</font></p>" +
                    "<p style=\"font-family: Arial, sans-serif;\"><font color=\"#FF0000\">" + banReason + "</font></p>" +
                    "<p style=\"font-family: Arial, sans-serif;\"><font color=\"#000000\">If you believe this ban is a mistake or have any questions, please contact our support team at <a href=\"mailto:trokino@gmail.com\">trokino@gmail.com</a>.</font></p>" +
                    "<p style=\"font-family: Arial, sans-serif;\"><font color=\"#000000\">Thank you.</font></p>" +
                    "<p style=\"font-family: Arial, sans-serif;\"><font color=\"#000000\">Best Regards,</font></p>" +
                    "<p style=\"font-family: Arial, sans-serif;\"><b><font color=\"#FF0000\">Your Company Name</font></b></p>" +
                    "</body>" +
                    "</html>";
            textPart.setContent(banMessage, "text/html");
            multipart.addBodyPart(textPart);
            message.setContent(multipart);
            session.setDebug(true);
            Transport.send(message);
            System.out.println("Ban notification email sent successfully!");
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }


}
