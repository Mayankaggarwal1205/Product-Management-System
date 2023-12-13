package com.smartContactManager.service;

import jakarta.mail.*;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import org.springframework.stereotype.Service;

import java.util.Properties;

@Service
public class EmailService {

    public Boolean sendEmail(String to, String from, String subject, String text){

        boolean flag = false;

        Properties properties = new Properties();

        // setting important information to properties object
        properties.put("mail.smtp.host", "smtp.gmail.com");
        properties.put("mail.smtp.port", "587");
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");

        String user = "gargmayank1205";
        String password = "vrvjnccpepdpllrt";

        // step 1
        Session session = Session.getInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(user, password);
            }
        });

        session.setDebug(true);

        // step 2 : compose the message [text, multimedia]

        try {

            Message mimeMessage = new MimeMessage(session);

            // from email
            mimeMessage.setFrom(new InternetAddress(from));

            // adding recipient to message
            mimeMessage.setRecipient(Message.RecipientType.TO, new InternetAddress(to));

            // adding subject to message
            mimeMessage.setSubject(subject);

            // adding text to message
//            mimeMessage.setText(text);

            // sending text to message in html format
            mimeMessage.setContent(text, "text/html");

            // step 3 : send the message using Transport class
            Transport.send(mimeMessage);

            System.out.println("Sent success.........");
            flag=true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return flag;
    }

}
