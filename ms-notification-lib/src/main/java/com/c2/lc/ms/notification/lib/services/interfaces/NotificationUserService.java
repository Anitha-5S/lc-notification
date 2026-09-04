package com.c2.lc.ms.notification.lib.services.interfaces;


import com.c2.lc.lib.bo.PageBO;
import com.c2.lc.lib.exceptions.RecordNotFoundException;
import com.c2.lc.lib.services.interfaces.BaseDBService;
import com.c2.lc.ms.notification.lib.entities.NotificationCategoryEntity;
import com.c2.lc.ms.notification.lib.entities.NotificationConfigEntity;
import com.c2.lc.ms.notification.lib.entities.NotificationUserEntity;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface NotificationUserService extends BaseDBService {

    void deleteNotification(Long id, JsonArray userId, String product_key);

    void addNotification(Long userId, String cFrom, String productKey, String customerId, Long categoryId, Long topicId, String title, String message, String options, LocalDateTime generatedAt);

    void markRead(Long userId, JsonArray notificationList, String product_key) ;

    void markAsUnread(Long userId, JsonArray notificationIdList, String product_key);

    List<NotificationUserEntity> listUnreadNotification(String userID, PageBO pageBO) throws RecordNotFoundException;

    JsonArray listAllNotification(String productKey, String topic, Long userId, PageBO pageBO) throws RecordNotFoundException;

    void deleteExpiredNotification(Long userId) throws RecordNotFoundException;

    NotificationUserEntity save(JsonObject jsonObject, NotificationConfigEntity configEntity, Optional<NotificationCategoryEntity> categoryEntity);

    void updateDeliveredTime(JsonArray userEntity) throws RecordNotFoundException;

    long unreadNotificationCount(String product_key, String userId);

    long listAllNotificationCount(String productKey, String topic, Long userId);
}
