package com.c2.lc.ms.notification.c.push.transactions.interfaces;

import com.c2.lc.lib.exceptions.InvalidRequestException;
import com.c2.lc.ms.notification.c.push.bo.EmailModel;
import org.springframework.messaging.MessagingException;

import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;

public interface SendEmailTransaction {

    void sendMailBasedOnFrom(EmailModel email) throws MessagingException, MalformedURLException, UnsupportedEncodingException, InvalidRequestException, javax.mail.MessagingException;

    void sendMail();
}
