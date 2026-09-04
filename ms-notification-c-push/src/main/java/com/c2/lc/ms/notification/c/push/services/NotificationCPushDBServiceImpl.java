package com.c2.lc.ms.notification.c.push.services;

import com.c2.lc.lib.services.BaseDBServiceImpl;
import com.c2.lc.ms.notification.c.push.services.interfaces.NotificationCPushDBService;
import com.c2.lc.ms.notification.lib.entities.NotificationConfigEntity;
import com.c2.lc.ms.notification.lib.entities.NotificationUserEntity;
import com.c2.lc.ms.notification.lib.repos.NotificationConfigRepo;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import com.google.gson.JsonObject;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Log4j2
@Service
public class NotificationCPushDBServiceImpl extends BaseDBServiceImpl implements NotificationCPushDBService {

    @Autowired
    private NotificationConfigRepo configRepo;

    private final FirebaseMessaging firebaseMessaging;

    public NotificationCPushDBServiceImpl(FirebaseMessaging firebaseMessaging) {
        this.firebaseMessaging = firebaseMessaging;
    }

    @Override
    public String pushNotification(NotificationUserEntity notificationUser) throws FirebaseMessagingException {

        System.out.println(notificationUser);
        System.out.println("---customer ID" + notificationUser.getCCustomerId() + "");
        System.out.println("---ProductKey ID" + notificationUser.getNotificationProduct().getCProductKey() + "");
        NotificationConfigEntity config = configRepo.getByPK(notificationUser.getCCustomerId(), notificationUser.getNotificationProduct().getCProductKey(),
                notificationUser.getNotificationTopic().getNTopicId(), "FIREBASE");

        Map<String, String> data = new HashMap<>();
        data.put("c_category", notificationUser.getNotificationCategory().getCCategory());
        data.put("c_topic", notificationUser.getNotificationTopic().getCTopic());
        data.put("c_action_id", notificationUser.getCActionId());
        data.put("c_product_name", notificationUser.getNotificationProduct().getCProductName());
        data.put("c_options", notificationUser.getCOptions());
        data.put("c_customer_id", notificationUser.getCCustomerId());

        Notification notification = Notification
                .builder()
                .setTitle(notificationUser.getCTitle())
                .setImage("")
                .setBody(notificationUser.getCMessage())
                .build();

        Message message = Message
                .builder()
                .setToken(config.getCServiceOptions())
                .setNotification(notification)
                .putAllData(data)
                .build();

        return firebaseMessaging.send(message);
    }

    @Override
    public void sendTelegramNotification(String telegramUrl, JsonObject requestPayload) {
        String result;
        JsonObject responseObject;
        //String alterUrl ="https://api.telegram.org/bot5504325370:AAFebig5S22g6vfzuq9QpqS3VDQ__RBfTsM/sendMessage";

        log.debug("API {}", telegramUrl);
        log.debug("Request {}", requestPayload.toString());

        result = callWebClientPostSyncApiWithHeader(telegramUrl, requestPayload.toString(), null);


        if (result == null || result.isEmpty()) {
            log.error("Result is null API {} -- Request {} -- Response {}", telegramUrl, requestPayload, result);
            responseObject = helper.getJsonObject(result);
        } else {
            responseObject = helper.getJsonObject(result);
            if (!responseObject.get("ok").getAsBoolean()) {
                log.error("API {} -- Request {} -- Response {}", telegramUrl, requestPayload, result);
            } else {
                log.debug("Response {}", result);
            }
        }
    }
}