package com.c2.lc.ms.notification.lib.services;


import com.c2.lc.lib.bo.PageBO;
import com.c2.lc.lib.exceptions.RecordNotFoundException;
import com.c2.lc.lib.services.BaseDBServiceImpl;
import com.c2.lc.lib.utils.Constants;
import com.c2.lc.ms.notification.lib.entities.*;
import com.c2.lc.ms.notification.lib.repos.*;
import com.c2.lc.ms.notification.lib.services.interfaces.NotificationUserService;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.persistence.Query;
import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class NotificationUserServiceImpl extends BaseDBServiceImpl implements NotificationUserService {

    @Autowired private NotificationTopicRepo notificationTopicRepo;
    @Autowired private NotificationProductRepo productRepo;
    @Autowired private NotificationCategoryRepo categoryRepo;
    @Autowired private NotificationUserRepo userRepo;
    @Autowired private NotificationConfigRepo configRepo;

    @Override
    public void deleteNotification(Long userId, JsonArray notificationList, String product_key)  {
        if (notificationList.size() > 0) {
            for (int i = 0; i < notificationList.size(); i++) {
                Long notificationId = notificationList.get(i).getAsLong();
                NotificationUserEntity userEntity = userRepo.findByUserId(helper.getString(userId), notificationId, product_key);
                if (userEntity != null) {
                    userEntity.setCStatus(Constants.STATUS_INACTIVE);
                    userRepo.save(userEntity);
                }
            }
        } else {
            List<NotificationUserEntity> entityList = userRepo.getUserNotifications(helper.getString(userId),product_key);
            for (NotificationUserEntity userEntity : entityList) {
                userEntity.setCStatus(Constants.STATUS_INACTIVE);
                userRepo.save(userEntity);
            }
        }
    }

    @Override
    public void addNotification(Long userId, String cFrom, String productKey, String customerId, Long categoryId, Long topicId, String title, String message, String options, LocalDateTime generatedAt) {

        Optional<NotificationTopicEntity> topicEntity = notificationTopicRepo.findById(topicId);
        Optional<NotificationCategoryEntity> categoryEntity = categoryRepo.findById(categoryId);
        Optional<NotificationProductEntity> productEntity = productRepo.findById(productKey);

        if (productEntity.isPresent() && topicEntity.isPresent() && categoryEntity.isPresent()) {

            NotificationUserEntity userEntity = new NotificationUserEntity();
            userEntity.setNotificationProduct(productEntity.get());
            userEntity.setNotificationCategory(categoryEntity.get());
            userEntity.setNotificationTopic(topicEntity.get());
            userEntity.setCFrom(cFrom);
            userEntity.setCMessage(message);
            userEntity.setCTitle(title);
            userEntity.setCOptions(options);
            userEntity.setCCustomerId(customerId);
            userEntity.setTGeneratedAt(generatedAt);
            /*userEntity.setTDeliveredAt(deliveredAt);
            userEntity.setTExpiresAt(expiresAt);//future date
            userEntity.setTReadAt(readAt);*/
            userRepo.save(userEntity);
        }
    }

    @Override
    public void markRead(Long userId, JsonArray notificationList, String product_key) {
        if (notificationList.size() > 0) {
            for (int i = 0; i < notificationList.size(); i++) {
                Long notificationId = notificationList.get(i).getAsLong();
                System.out.println(userId);
                NotificationUserEntity entity = userRepo.findByUserId(helper.getString(userId), notificationId,product_key);
                if (entity != null) {
                    entity.setTReadAt(helper.getCurrentTime());
                    userRepo.save(entity);
                }
            }
        } else {
            List<NotificationUserEntity> entityList = userRepo.getUserNotifications(helper.getString(userId),product_key);
            for (NotificationUserEntity userEntity : entityList) {
                userEntity.setTReadAt(helper.getCurrentTime());
                userRepo.save(userEntity);
            }
        }
    }

    @Override
    public void markAsUnread(Long userId, JsonArray notificationIdList, String product_key) {
        if (notificationIdList.size() > 0) {
            for (int i = 0; i < notificationIdList.size(); i++) {
                Long notificationId = notificationIdList.get(i).getAsLong();
                NotificationUserEntity entity = userRepo.findByUserId(helper.getString(userId), notificationId, product_key);
                if (entity != null) {
                    entity.setTReadAt(null);
                    userRepo.save(entity);
                }
            }
        } else {
            List<NotificationUserEntity> entityList = userRepo.getUserNotifications(helper.getString(userId), product_key);
            for (NotificationUserEntity userEntity : entityList) {
                userEntity.setTReadAt(null);
                userRepo.save(userEntity);
            }
        }
    }

    @Override
    public List<NotificationUserEntity> listUnreadNotification(String userID, PageBO pageBO) throws RecordNotFoundException {
        Pageable pageable = PageRequest.of(pageBO.getPage(), pageBO.getLimit());
        List<NotificationUserEntity> unreadNotifications = userRepo.getUnreadNotifications(userID, pageable);
        if (unreadNotifications.isEmpty()) {
            throw new RecordNotFoundException("Record Not Found");
        }
        return unreadNotifications;
    }

    @Override
    public JsonArray listAllNotification(String productKey, String topic, Long userId, PageBO pageBO) throws RecordNotFoundException {
        String sql = getAllNotifications((userId.toString()),productKey,topic);
        Query allNotifications = this.getQuery(sql);
        List<Object[]> resultList = this.getResultList(allNotifications,pageBO.getPage(),pageBO.getLimit());

        if (resultList.isEmpty()) {
            throw new RecordNotFoundException("Record Not Found");
        }
        JsonArray jsonArray = new JsonArray();
        for(Object[] objects: resultList)
        {
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("n_notification_id", BigInteger.valueOf(helper.getLong(helper.getString(objects[0]))));
            jsonObject.addProperty("c_customer_id",helper.getString(objects[1]));
            jsonObject.addProperty("c_product_key",helper.getString(objects[2]));
            jsonObject.addProperty("c_product_name",helper.getString(objects[3]));
            jsonObject.addProperty("c_category",helper.getString(objects[4]));
            jsonObject.addProperty("c_topic",helper.getString(objects[5]));
            jsonObject.addProperty("c_title",helper.getString(objects[6]));
            jsonObject.addProperty("c_from",helper.getString(objects[7]));
            jsonObject.addProperty("c_message",helper.getString(objects[8]));
            jsonObject.addProperty("c_action_id",helper.getString(objects[9]));
            jsonObject.addProperty("t_generated_at",helper.getString(objects[10]));
            jsonObject.addProperty("t_read_at",helper.getString(objects[11]));
            jsonObject.addProperty("t_delivered_at",helper.getString(objects[12]));
            jsonObject.addProperty("c_options",helper.getString(objects[13]));
            jsonObject.addProperty("c_status",helper.getString(objects[14]));
            jsonArray.add(jsonObject);
        }
        return jsonArray;
    }

    private String getAllNotifications(String userId, String productKey, String topic) {
        String sql = "SELECT DISTINCT nu.n_notification_id,nc2.c_customer_id ,  " +
                "nu.c_product_key, np.c_product_name, nc.c_category , nt.c_topic,  " +
                "nu.c_title, nu.c_from, nu.c_message,nu.c_action_id, nu.t_generated_at, nu.t_read_at, nu.t_delivered_at, nu.c_options, nu.c_status   " +
                "FROM notification_user nu  " +
                "JOIN notification_topic nt on nu.n_topic_id = nt.n_topic_id and nt.c_product_key = nu.c_product_key   " +
                "JOIN notification_category nc on nu.n_category_id = nc.n_category_id   " +
                "JOIN notification_product np on nu.c_product_key = np.c_product_key   " +
                "JOIN notification_config nc2 on nu.c_customer_id = nc2.c_customer_id and nc2.n_topic_id = nt.n_topic_id   " +
                "WHERE nu.c_status = 'A' AND nu.c_customer_id = '" + userId + "' " +
                "AND np.c_product_key = '" + productKey + "' ";
        if (!topic.equals(com.c2.lc.ms.notification.lib.utils.Constants.TS_NOTIFICATION_ALL) && productKey.equals("TS"))
            sql += " AND nt.c_topic = '" + topic + "' ";
        sql += "ORDER BY t_generated_at DESC ";
        return sql;
    }

    @Override
    public void deleteExpiredNotification(Long userId) throws RecordNotFoundException {
        List<NotificationUserEntity> userEntityList = userRepo.deleteExpired(String.valueOf(userId));
        if (userEntityList.size() == 0) {
            throw new RecordNotFoundException("Record Not Found");
        }
        for (NotificationUserEntity notificationUserEntity : userEntityList) {
            NotificationUserEntity entity = userRepo.getById(notificationUserEntity.getNNotificationId());
            entity.setCStatus(Constants.STATUS_INACTIVE);
            userRepo.save(entity);
        }
    }

    @Override
    public NotificationUserEntity save(JsonObject jsonObject, NotificationConfigEntity configEntity, Optional<NotificationCategoryEntity> categoryEntity) {
        System.out.println(jsonObject);
                NotificationUserEntity userEntity = new NotificationUserEntity();

                if (configEntity != null && configEntity.getCServiceOptions() != null && categoryEntity.isPresent()) {
                    userEntity.setNotificationProduct(configEntity.getNotificationProduct());
                    userEntity.setNotificationCategory(categoryEntity.get());
                    userEntity.setNotificationTopic(configEntity.getNotificationTopic());
                    userEntity.setCFrom(jsonObject.get("c_from").getAsString());
                    userEntity.setCMessage(jsonObject.get("c_message").getAsString());
                    userEntity.setCTitle(jsonObject.get("c_title").getAsString());
                    userEntity.setCOptions(jsonObject.get("c_options").getAsString());
                    userEntity.setCCustomerId(jsonObject.get("c_customer_id").getAsString());
                    userEntity.setCActionId(jsonObject.has("c_action_id") ? jsonObject.get("c_action_id").getAsString() :"");
                    userEntity.setTGeneratedAt(helper.getCurrentTime());
                    if(!configEntity.getCServiceName().equals("EMAIL")) {
                        LocalDateTime minusDays = helper.getCurrentTime().plusDays(configEntity.getNDeleteAfter());
                        userEntity.setTExpiresAt(minusDays);
                    }
                    userRepo.save(userEntity);
                }

        return userEntity;
    }

    @Override
    public void updateDeliveredTime(JsonArray notificationList) throws RecordNotFoundException {
        if (notificationList.size() > 0) {
            for (int i = 0; i < notificationList.size(); i++) {
                Long notificationId = notificationList.get(i).getAsLong();
                NotificationUserEntity user = userRepo.getById(notificationId);
                if (!helper.isEmpty(user)) {
                    user.setTDeliveredAt(helper.getCurrentTime());
                    userRepo.save(user);
                } else {
                    throw new RecordNotFoundException("Record Not Found");
                }
            }
        } else {
            throw new RecordNotFoundException("NotificationList is empty");
        }
    }

    @Override
    public long unreadNotificationCount(String product_key, String userId) {
        return userRepo.unreadNotificationsCount(product_key,userId);
    }

    @Override
    public long listAllNotificationCount(String productKey, String topic, Long userId) {
        BigInteger count = BigInteger.ZERO;
        String sql = getAllNotifications(userId.toString(),productKey,topic);
        Query query = this.getQuery("SELECT COUNT(*) FROM (" + sql + ") DUMMY");
        Object result = this.getSingleResult(query);

        if (result != null) {
            count = (BigInteger) result;
        }

        return count.intValue();
    }
}
