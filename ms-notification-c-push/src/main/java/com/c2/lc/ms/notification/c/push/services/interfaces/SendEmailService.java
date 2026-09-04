package com.c2.lc.ms.notification.c.push.services.interfaces;


import com.c2.lc.ms.notification.c.push.bo.EmailModel;
import javax.mail.MessagingException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;

public interface SendEmailService {

    void sendEmailNoreplayMicroservicesbasedOnDomain(EmailModel email) throws MessagingException, UnsupportedEncodingException, MalformedURLException;

    void sendEmailNoreplayliveconnectbasedOnDomain(EmailModel email) throws MessagingException, MalformedURLException;
}
