package com.c2.lc.ms.notification.p.transactions;

import com.azure.messaging.eventhubs.EventData;
import com.c2.lc.lib.eventhub.EventHubUtil;
import com.c2.lc.ms.notification.lib.services.interfaces.NotificationCategoryService;
import com.c2.lc.ms.notification.p.transactions.interfaces.NotificationTransaction;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class NotificationTransactionImpl implements NotificationTransaction {

    @Autowired private NotificationCategoryService notificationCategoryService ;

    @Value("${eventhub.name}")
    private String eventHubForNotification;

    @Value("${eventhub.connection.string}")
    private String connectionString;


    @Override
    public void notificationEventHub(JsonArray data) {
        List<EventData> allEvents = new ArrayList<>();
        for (JsonElement row : data) {
            allEvents.add(new EventData(row.getAsJsonObject().toString()));
        }
        EventHubUtil.publishEvents(connectionString, eventHubForNotification, allEvents);
    }
}

