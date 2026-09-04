package com.c2.lc.ms.notification.c.push.services;


import com.c2.lc.ms.notification.c.push.bo.AttachMentModel;
import com.c2.lc.ms.notification.c.push.bo.EmailModel;
import com.c2.lc.ms.notification.c.push.bo.ToListModel;
import com.c2.lc.ms.notification.c.push.services.interfaces.SendEmailService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.activation.DataHandler;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.util.ByteArrayDataSource;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Base64;
import java.util.List;
import java.util.Objects;
import java.util.Properties;

@Slf4j
@Service
public class SendEmailServiceImpl implements SendEmailService {


    @Value("${gmail.smtp.user}") private String smtpUser;
    @Value("${gmail.smtp.userName}") private String userName;
    @Value("${gmail.smtp.password}") private String password;


    private Multipart getContentWithAttachmentsFromEncodedString(EmailModel email) throws MessagingException, MalformedURLException {

        Multipart multipart = new MimeMultipart();

//      Adding content to the email
        BodyPart messageContent = new MimeBodyPart();
        messageContent.setContent(email.getContent(), "text/html");
        multipart.addBodyPart(messageContent);

//      Adding attachments to the body
        List<AttachMentModel> attachments = email.getAttachments();
        if (attachments != null && attachments.size() > 0) {
            for (AttachMentModel attachment : attachments) {
                MimeBodyPart attachmentMimeBodyPart = new MimeBodyPart();

                if(attachment.getFileData().substring(0,4).equals("http")){
                    attachmentMimeBodyPart.setDataHandler(new DataHandler(new URL(attachment.getFileData())));
                }else {

                    String[] encodedFile = attachment.getFileData().split("[,]");
                    byte[] imageData = Base64.getMimeDecoder().decode(encodedFile[1]);
                    String[] contentType = encodedFile[0].split(";");
                    String content = contentType[0].split(":")[1].replace("@","");
                    ByteArrayDataSource rawData= new ByteArrayDataSource(imageData,content);
                    attachmentMimeBodyPart.setDataHandler(new DataHandler(rawData));
                }
                attachmentMimeBodyPart.setFileName(attachment.getFileName());
                multipart.addBodyPart(attachmentMimeBodyPart);
            }
        }
        return multipart;

    }

    @Override
    public void sendEmailNoreplayMicroservicesbasedOnDomain(EmailModel email) throws MessagingException, MalformedURLException {

        Properties props = new Properties();

        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "465");
        props.put("mail.smtp.user", smtpUser);
        props.put("mail.smtp.debug", "true");
        props.put("mail.smtp.socketFactory.port", "465");
        props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        props.put("mail.smtp.socketFactory.fallback", "false");


        Session session = Session.getInstance(props, new javax.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(userName, password);
            }
        });
        Message msg = new MimeMessage(session);

        addToRecipients(email, msg);

        InternetAddress[] fromAddress = new InternetAddress[1];
        fromAddress[0] = new InternetAddress(email.getFrom());

        msg.setSubject(email.getSubject());
        Multipart multipart = getContentWithAttachmentsFromEncodedString(email);
        msg.setContent(multipart);
        Transport.send(msg);
        log.debug("Email sent to : {}", email.getToList());

    }

    @Override
    public void sendEmailNoreplayliveconnectbasedOnDomain(EmailModel email) throws MessagingException, MalformedURLException {

        Properties props = new Properties();

        //props.put("mail.transport.protocol","smtp");
        props.put("mail.smtp.auth", "true");
       // props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.ssl.enable", "false");
        props.put("mail.smtp.host", "smtpout.secureserver.net");
        props.put("mail.smtp.port", "25");
        props.put("mail.smtp.user", "noreply.liveconnect@csquare.in");
        props.put("mail.smtp.debug", "true");
        props.put("mail.smtp.UseDefaultCredentials","false");
        //props.put("mail.smtp.socketFactory.port", "465");
        //props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        //props.put("mail.smtp.socketFactory.fallback", "false");


        Session session = Session.getInstance(props, new javax.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication("noreply.liveconnect@csquare.in", "Live#2022");
            }
        });
        Message msg = new MimeMessage(session);

        addToRecipients(email, msg);

        InternetAddress[] fromAddress = new InternetAddress[1];
        fromAddress[0] = new InternetAddress(email.getFrom());

        msg.setSubject(email.getSubject());
        Multipart multipart = getContentWithAttachmentsFromEncodedString(email);
        msg.setContent(multipart);
        Transport.send(msg);
        log.debug("Email sent to : {}", email.getToList());

    }

    private void addToRecipients(EmailModel email, Message msg) throws MessagingException {

        ToListModel toList = email.getToList();

        if (toList != null) {

            // adding to
            if (Objects.requireNonNull(toList.getTo()).size() > 0) {
                InternetAddress[] recipientAddress = new InternetAddress[email.getToList().getTo().size()];
                int counter = 0;
                for (String recipient : email.getToList().getTo()) {
                    recipientAddress[counter] = new InternetAddress(recipient);
                    counter++;
                }
                msg.setRecipients(Message.RecipientType.TO, recipientAddress);
            }

            // adding cc
            if (toList.getToCc() != null && toList.getToCc().size() > 0) {
                InternetAddress[] recipientAddress = new InternetAddress[email.getToList().getToCc().size()];
                int counter = 0;
                for (String recipient : email.getToList().getToCc()) {
                    recipientAddress[counter] = new InternetAddress(recipient);
                    counter++;
                }
                msg.setRecipients(Message.RecipientType.CC, recipientAddress);
            }

            // adding bcc
            if (toList.getToBcc() != null && toList.getToBcc().size() > 0) {
                InternetAddress[] recipientAddress = new InternetAddress[email.getToList().getToBcc().size()];
                int counter = 0;
                for (String recipient : email.getToList().getToBcc()) {
                    recipientAddress[counter] = new InternetAddress(recipient);
                    counter++;
                }
                msg.setRecipients(Message.RecipientType.BCC, recipientAddress);
            }
        }

    }

}
