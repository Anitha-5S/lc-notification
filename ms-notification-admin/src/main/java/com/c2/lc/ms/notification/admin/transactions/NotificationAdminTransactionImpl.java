package com.c2.lc.ms.notification.admin.transactions;

import com.c2.lc.lib.bo.PageBO;
import com.c2.lc.lib.exceptions.DuplicateRecordException;
import com.c2.lc.lib.exceptions.RecordNotFoundException;
import com.c2.lc.lib.transactions.BaseTransactionImpl;
import com.c2.lc.ms.notification.admin.transactions.interfaces.NotificationAdminTransaction;
import com.c2.lc.ms.notification.lib.entities.*;
import com.c2.lc.ms.notification.lib.services.interfaces.*;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
public class NotificationAdminTransactionImpl extends BaseTransactionImpl implements NotificationAdminTransaction {

    @Autowired private NotificationCategoryService notificationCategoryService;
    @Autowired private NotificationProductService notificationProductService;
    @Autowired private NotificationTopicService notificationTopicService;
    @Autowired private NotificationServiceService notificationService;
    @Autowired private NotificationUserService notificationUserService;
    @Autowired private NotificationConfigService notificationConfigService;

    @Override
    public void addCategory(Long userId, String category) throws DuplicateRecordException {
        notificationCategoryService.add(userId,category);
    }

    @Override
    public List<NotificationCategoryEntity> listCategory() throws RecordNotFoundException {
        return notificationCategoryService.list();
    }

    @Override
    public void delete(Long userId, Long categoryId) throws RecordNotFoundException {
        notificationCategoryService.delete(userId, categoryId);
    }

    @Override
    public void addProduct(Long userId, String productKey, String productName) throws DuplicateRecordException {
        notificationProductService.add(userId, productKey, productName);
    }

    @Override
    public List<NotificationProductEntity> listProduct() throws RecordNotFoundException {
        return notificationProductService.list();
    }

    @Override
    public void deleteProduct(Long userId, String productKey) throws RecordNotFoundException {
        notificationProductService.deleteProduct(userId, productKey);
    }

    @Override
    public List<String> addTopic(Long userId, JsonArray topics, String productKey) throws RecordNotFoundException {
        return notificationTopicService.add(userId, topics, productKey);
    }

    @Override
    public List<NotificationTopicEntity> listTopic(String productKey) throws RecordNotFoundException {
        return notificationTopicService.list(productKey);
    }

    @Override
    public void deleteTopic(Long userId, Long topicId) throws RecordNotFoundException {
        notificationTopicService.delete(userId, topicId);
    }

    @Override
    public List<NotificationServiceEntity> listService() throws RecordNotFoundException {
        return notificationService.listService();
    }

    @Override
    public void deleteService(Long userId, String serviceName) throws RecordNotFoundException {
        notificationService.deleteService(userId, serviceName);
    }

    @Override
    public void addService(Long userId, String serviceName, String endPoint, String description) throws DuplicateRecordException {
        notificationService.addService(userId, serviceName, endPoint, description);
    }

    @Override
    public void deleteNotification(Long id, JsonArray notificationId, String product_key) {
        notificationUserService.deleteNotification(id,notificationId,product_key);
    }

    @Override
    public void addNotification(Long userId, JsonObject data){
        NotificationUserEntity notificationUser = helper.fromJson(data,NotificationUserEntity.class);
        Long categoryId = data.get("n_category_id").getAsLong();
        String productKey = data.get("c_product_key").getAsString();
        Long topicId = data.get("n_topic_id").getAsLong();
        String customerId = data.get("c_customer_id").getAsString();
        String title = data.get("c_title").getAsString();
        String message = data.get("c_message").getAsString();
        String cFrom = data.get("c_from").getAsString();
        String options = data.get("c_options").getAsString();
        LocalDateTime generatedAt = helper.getCurrentTime();

        notificationUserService.addNotification(userId, cFrom, productKey, customerId, categoryId, topicId, title, message, options, generatedAt);
    }

    @Override
    public List<NotificationConfigEntity> ListConfig(String status, String customerId) throws RecordNotFoundException {
        return notificationConfigService.listConfig(status, customerId);
    }

    @Override
    public void addConfig(Long userId, String customerId, String productKey, Long topicId, String serviceName, String serviceOptions, Long deleteAfter) throws Exception {
        notificationConfigService.addConfig(userId, customerId, productKey, topicId, serviceName, serviceOptions, deleteAfter);
    }

    @Override
    public void markNotificationRead(Long userId, JsonArray notificationList, String product_key)  {
        notificationUserService.markRead(userId,notificationList,product_key);
    }

    @Override
    public void markAsUnread(Long userId, JsonArray notificationIdList, String product_key)  {
        notificationUserService.markAsUnread(userId,notificationIdList,product_key);
    }

    @Override
    public List<NotificationUserEntity> listUnreadNotification(String userID, PageBO pageBO) throws RecordNotFoundException {
        return notificationUserService.listUnreadNotification(userID,pageBO);
    }

    @Override
    public JsonArray listAllNotification(String productKey, String topic, Long userId, PageBO pageBO) throws RecordNotFoundException {
        return notificationUserService.listAllNotification(productKey,topic,userId,pageBO);
    }

    @Override
    public void deleteExpiredNotification(Long userId) throws RecordNotFoundException {
        notificationUserService.deleteExpiredNotification(userId);
    }

    @Override
    public void deleteConfig(Long userId, String productKey, String serviceName, String customerId, Long topicId) throws RecordNotFoundException {
        notificationConfigService.deleteConfig(userId, productKey, serviceName, customerId, topicId);

    }

    @Override
    public List<String> updateConfig(Long userId, String productKey, String serviceName, JsonArray topics, String deviceToken) throws RecordNotFoundException {
        return notificationConfigService.updateToken(userId,productKey,serviceName,topics,deviceToken);
    }

    @Override
    public void updateDeliveredNotification(JsonArray notificationId) throws RecordNotFoundException {
        notificationUserService.updateDeliveredTime(notificationId);
    }

    @Override
    public long listAllNotificationCount(String productKey, String topic, Long userId) {
        return notificationUserService.listAllNotificationCount(productKey,topic,userId);
    }

    @Override
    public long unreadNotificationCount(String product_key, String userId) {
        return notificationUserService.unreadNotificationCount(product_key,userId);
    }
}
