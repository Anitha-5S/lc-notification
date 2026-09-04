package com.c2.lc.ms.notification.lib.services;


import com.c2.lc.lib.exceptions.RecordNotFoundException;
import com.c2.lc.lib.services.BaseDBServiceImpl;
import com.c2.lc.lib.utils.Constants;
import com.c2.lc.ms.notification.lib.entities.NotificationProductEntity;
import com.c2.lc.ms.notification.lib.entities.NotificationTopicEntity;
import com.c2.lc.ms.notification.lib.repos.NotificationProductRepo;
import com.c2.lc.ms.notification.lib.repos.NotificationTopicRepo;
import com.c2.lc.ms.notification.lib.services.interfaces.NotificationTopicService;
import com.google.gson.JsonArray;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Log4j2
@Service
public class NotificationTopicServiceImpl extends BaseDBServiceImpl implements NotificationTopicService {

    @Autowired private NotificationTopicRepo notificationTopicRepo;
    @Autowired private NotificationProductRepo productRepo;

    @Override
    public List<String> add(Long userId, JsonArray topics, String productKey) throws RecordNotFoundException {
        List<String> addedTopics = new ArrayList<>();
        Optional<NotificationProductEntity> productEntity = productRepo.findById(productKey);
        if (productEntity.isPresent()) {
            for (int i = 0; i < topics.size(); i++) {
                Optional<NotificationTopicEntity> notificationTopic = notificationTopicRepo.findByTopic(topics.get(i).getAsString(), productKey);
                if (notificationTopic.isEmpty()) {
                    NotificationTopicEntity topicEntity = new NotificationTopicEntity();
                    NotificationProductEntity entity = productEntity.get();
                    topicEntity.setNotificationProduct(entity);
                    topicEntity.setCTopic(topics.get(i).getAsString());
                    topicEntity.setIdTime(userId, helper.getCurrentTime());
                    notificationTopicRepo.save(topicEntity);
                    addedTopics.add(topics.get(i).getAsString());
                } else
                    log.info("Topic '"+topics.get(i).getAsString()+"' is already exists for this product!");
            }
        } else
            throw new RecordNotFoundException("product Not Found");
        return addedTopics;
    }

    @Override
    public List<NotificationTopicEntity> list(String productKey) throws RecordNotFoundException {
        List<NotificationTopicEntity> TopicList = notificationTopicRepo.getAllTopicsByProduct(productKey);
        if (TopicList.isEmpty()) {
            throw new RecordNotFoundException("Record not found");
        }
        return TopicList;
    }

    @Override
    public void delete(Long userId, Long topicId) throws RecordNotFoundException {
        NotificationTopicEntity topicEntity = notificationTopicRepo.getById(topicId);
        if (!helper.isEmpty(topicEntity)) {
            topicEntity.setLastUpdated(userId, helper.getCurrentTime());
            topicEntity.setCStatus(Constants.STATUS_INACTIVE);
            notificationTopicRepo.save(topicEntity);
        } else {
            throw new RecordNotFoundException("Record not found");
        }
    }
}
