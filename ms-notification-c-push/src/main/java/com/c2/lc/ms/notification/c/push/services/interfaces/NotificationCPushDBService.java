package com.c2.lc.ms.notification.c.push.services.interfaces;

import com.c2.lc.lib.services.interfaces.BaseDBService;
import com.c2.lc.ms.notification.lib.entities.NotificationUserEntity;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.gson.JsonObject;

public interface NotificationCPushDBService extends BaseDBService {
    String pushNotification(NotificationUserEntity jsonObject) throws FirebaseMessagingException;

    void sendTelegramNotification(String configEntity, JsonObject userEntity);
}
