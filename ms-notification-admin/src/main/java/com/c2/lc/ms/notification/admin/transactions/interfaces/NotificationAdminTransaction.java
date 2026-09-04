package com.c2.lc.ms.notification.admin.transactions.interfaces;

import com.c2.lc.lib.bo.PageBO;
import com.c2.lc.lib.exceptions.DuplicateRecordException;
import com.c2.lc.lib.exceptions.RecordNotFoundException;
import com.c2.lc.lib.transactions.interfaces.BaseTransaction;
import com.c2.lc.ms.notification.lib.entities.*;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.util.List;

public interface NotificationAdminTransaction extends BaseTransaction {

    void addCategory(Long userId, String category) throws DuplicateRecordException;

    List<NotificationCategoryEntity> listCategory() throws RecordNotFoundException;

    void delete(Long userId, Long entity) throws RecordNotFoundException;

    void addProduct(Long userId, String productKey, String productName) throws DuplicateRecordException;

    List<NotificationProductEntity> listProduct() throws RecordNotFoundException;

    void deleteProduct(Long userId, String productKey) throws RecordNotFoundException;

    List<String> addTopic(Long userId, JsonArray topics, String productKey) throws RecordNotFoundException;

    List<NotificationTopicEntity> listTopic(String productKey) throws RecordNotFoundException;

    void deleteTopic(Long userId,  Long topicId) throws RecordNotFoundException;

    List<NotificationServiceEntity> listService() throws RecordNotFoundException;

    void deleteService(Long userId, String serviceName) throws RecordNotFoundException;

    void addService(Long userId, String serviceName, String endPoint, String description) throws DuplicateRecordException;

    void deleteNotification(Long id, JsonArray userId, String c_product_key) ;

    void addNotification(Long userId, JsonObject data) ;

    List<NotificationConfigEntity> ListConfig(String status, String customerId) throws RecordNotFoundException;

    void addConfig(Long userId, String customerId, String productKey, Long topicId, String serviceName, String serviceOptions, Long deleteAfter) throws Exception;

    void markNotificationRead(Long userId, JsonArray notificationId, String c_product_key) ;

    void markAsUnread(Long userId, JsonArray notificationId, String c_product_key) ;

    List<NotificationUserEntity> listUnreadNotification(String string, PageBO pageBO) throws RecordNotFoundException;

    JsonArray listAllNotification(String productKey, String topic, Long userId, PageBO pageBO) throws RecordNotFoundException;

    void deleteExpiredNotification(Long userId) throws RecordNotFoundException;

    void deleteConfig(Long userId, String productKey, String serviceName, String customerId, Long topicId) throws RecordNotFoundException;

    List<String> updateConfig(Long userId, String productKey, String serviceName, JsonArray topics, String deviceToken) throws RecordNotFoundException;

    void updateDeliveredNotification(JsonArray notificationId) throws RecordNotFoundException;

    long unreadNotificationCount(String c_product_key, String userId);

    long listAllNotificationCount(String productKey, String topic, Long userId);
}
