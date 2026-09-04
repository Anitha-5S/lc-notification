package com.c2.lc.ms.notification.c.push.transactions;


import com.c2.lc.lib.exceptions.InvalidRequestException;
import com.c2.lc.lib.transactions.BaseTransactionImpl;
import com.c2.lc.ms.notification.c.push.bo.EmailModel;
import com.c2.lc.ms.notification.c.push.services.interfaces.SendEmailService;
import com.c2.lc.ms.notification.c.push.transactions.interfaces.SendEmailTransaction;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.PasswordAuthentication;
import java.util.Properties;

@Slf4j
@Component
public class SendEmailTransactionImpl extends BaseTransactionImpl implements SendEmailTransaction {

    @Autowired private SendEmailService sendEmailService;

    @Override
    public void sendMailBasedOnFrom(EmailModel email) throws MessagingException, MalformedURLException, UnsupportedEncodingException, InvalidRequestException {
        switch (email.getFrom()) {
            case "noreply.microservices@c2info.com":
                sendEmailService.sendEmailNoreplayMicroservicesbasedOnDomain(email);
                break;

            case "noreply.liveconnect@csquare.in":
                sendEmailService.sendEmailNoreplayliveconnectbasedOnDomain(email);
                break;

            default:
                log.error("{} is not added", email.getFrom());
                throw new InvalidRequestException("response", "Invalid requestbody " + email.getFrom() + " is not added");


        }
    }

        public void sendMail(){

            String to = "anithalakshan5@gmail.com";//change accordingly

            String from = "anitalak.shan@c2info.com";

            log.debug("Notification c-push log");
            //Get the session object
            Properties props = new Properties();
            props.put("mail.smtp.host", "smtp.gmail.com");
            props.put("mail.smtp.socketFactory.port", "465");
            props.put("mail.smtp.socketFactory.class",
                    "javax.net.ssl.SSLSocketFactory");
            props.put("mail.smtp.auth", "true");
            props.put("mail.smtp.port", "465");
            //get Session
            Session session = Session.getInstance(props, new javax.mail.Authenticator() {
                protected javax.mail.PasswordAuthentication getPasswordAuthentication() {
                    return new javax.mail.PasswordAuthentication(from,"aszpghtvaaqhqjql");
                }
            });
            //compose message
            try {
                MimeMessage message = new MimeMessage(session);
                message.addRecipient(Message.RecipientType.TO,new InternetAddress(to));
                message.setSubject("Testing gmail");
                message.setText("From notification service testing gmail");
                //send message
                Transport.send(message);
                log.debug("message sent successfully - from notification c-push");
            } catch (MessagingException e) {throw new RuntimeException(e);}

        }
}


