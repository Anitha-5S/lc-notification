package com.c2.lc.ms.notification.p.transactions.interfaces;

import com.google.gson.JsonArray;

public interface NotificationTransaction {
    void notificationEventHub(JsonArray data);
}
