package com.smartContactManager.service;

import jakarta.mail.*;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeBodyPart;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.internet.MimeMultipart;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.Properties;

@Service
public class EmailServiceAttachment {


    public Boolean sendEmailAttachment(String to, String from, String subject, String text, File file){

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

            // adding text to message where attahment and text both are present

            // In part1 we just set text which we have passes
            MimeBodyPart part1 = new MimeBodyPart();
            part1.setText(text);

            // In part2 we have set attachment file
            MimeBodyPart part2 = new MimeBodyPart();
            part2.attachFile(file);

            // In mimeMultipart we have combined both parts and make it one
            MimeMultipart mimeMultipart = new MimeMultipart();
            mimeMultipart.addBodyPart(part1);
            mimeMultipart.addBodyPart(part2);

            // here we have set content which we want to pass in email
            mimeMessage.setContent(mimeMultipart);


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
