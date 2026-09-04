package com.c2.lc.ms.notification.lib.services;


import com.c2.lc.lib.exceptions.RecordNotFoundException;
import com.c2.lc.lib.services.BaseDBServiceImpl;
import com.c2.lc.lib.utils.Constants;
import com.c2.lc.ms.notification.lib.entities.*;
import com.c2.lc.ms.notification.lib.repos.*;
import com.c2.lc.ms.notification.lib.services.interfaces.NotificationConfigService;
import com.google.gson.JsonArray;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Log4j2
@Service
public class NotificationConfigServiceImpl extends BaseDBServiceImpl implements NotificationConfigService {

    @Autowired private NotificationTopicRepo notificationTopicRepo;
    @Autowired private NotificationProductRepo productRepo;
    @Autowired private NotificationCategoryRepo categoryRepo;
    @Autowired private NotificationUserRepo userRepo;
    @Autowired private NotificationConfigRepo configRepo;
    @Autowired private NotificationServiceRepo serviceRepo;

    public static final String FIREBASE = "FIREBASE";

    @Override
    public List<NotificationConfigEntity> listConfig(String status, String customerId) throws RecordNotFoundException {
        List<NotificationConfigEntity> configList = configRepo.getConfigList(status, customerId);
        if (helper.isEmpty(configList)) {
            throw new RecordNotFoundException("Record Not Found");
        }
        return configList;
    }

    @Override
    public void addConfig(Long userId, String customerId, String productKey, Long topicId, String serviceName, String serviceOptions, Long deleteAfter) throws Exception {

      /*  List<NotificationConfigEntity> configEntityList = configRepo.findByPk(userId);
        if(configEntityList.size() > 0){
            updateToken(userId, productKey, serviceName, serviceOptions);
        }
*/
        Optional<NotificationProductEntity> productEntity = productRepo.findById(productKey);
        Optional<NotificationTopicEntity> topicEntity = notificationTopicRepo.findById(topicId);
        Optional<NotificationServiceEntity> serviceEntity = serviceRepo.findById(serviceName);

        if (productEntity.isPresent() && topicEntity.isPresent() && serviceEntity.isPresent()) {
            NotificationConfigEntity configEntity = new NotificationConfigEntity();
            configEntity.setNotificationProduct(productEntity.get());
            configEntity.setNotificationTopic(topicEntity.get());
            configEntity.setNotificationService(serviceEntity.get());
            configEntity.setCCustomerId(customerId);
            configEntity.setCProductKey(productKey);
            configEntity.setNTopicId(topicId);
            configEntity.setCServiceName(serviceName);
            configEntity.setIdTime(userId, helper.getCurrentTime());
            configEntity.setCServiceOptions(serviceOptions);
            configEntity.setNDeleteAfter(deleteAfter);
            configRepo.save(configEntity);
        } else {
            throw new RecordNotFoundException("Record Not Found");
        }
    }

    @Override
    public void deleteConfig(Long userId, String productKey, String serviceName, String customerId, Long topicId) throws RecordNotFoundException {
        Optional<NotificationProductEntity> productEntity = productRepo.findById(productKey);
        Optional<NotificationTopicEntity> topicEntity = notificationTopicRepo.findById(topicId);
        Optional<NotificationServiceEntity> serviceEntity = serviceRepo.findById(serviceName);
        if (productEntity.isPresent() && topicEntity.isPresent() && serviceEntity.isPresent()) {
            NotificationConfigEntity entity = configRepo.getByPK(customerId, productEntity.get().getCProductKey(), topicEntity.get().getNTopicId(), serviceEntity.get().getCServiceName());
            entity.setCStatus(Constants.STATUS_INACTIVE);
            configRepo.save(entity);
        } else
            throw new RecordNotFoundException("Record Not Found");
    }

    @Override
    public List<String> updateToken(Long userId, String productKey, String serviceName, JsonArray topics, String deviceToken) throws RecordNotFoundException {
        List<String> topicList = new ArrayList<>();
        List<String> response = new ArrayList<>();
        for (int i = 0; i < topics.size(); i++) {
            topicList.add(topics.get(i).getAsString());
        }
        NotificationProductEntity productEntity = productRepo.getById(productKey);
        if (Objects.equals(serviceName, FIREBASE)) {
            if (!helper.isEmpty(productEntity)) {
                for (String topic : topicList) {
                    Optional<NotificationTopicEntity> topicEntity = notificationTopicRepo.findByTopic(topic, productKey);
                    if (topicEntity.isPresent()) {
                        NotificationConfigEntity notificationConfig = configRepo.getByPK(String.valueOf(userId), productKey, topicEntity.get().getNTopicId(), serviceName);
                        if (notificationConfig != null) {
                            notificationConfig.setCServiceOptions(deviceToken);
                            notificationConfig.setTLastUpdatedAt(helper.getCurrentTime());
                            configRepo.save(notificationConfig);
                        } else {
                            NotificationConfigEntity configEntity = new NotificationConfigEntity();
                            assert false;
                            configEntity.setNTopicId(topicEntity.get().getNTopicId());
                            configEntity.setCServiceName(serviceName);
                            configEntity.setCCustomerId(String.valueOf(userId));
                            configEntity.setCProductKey(productKey);
                            configEntity.setCServiceOptions(deviceToken);
                            configEntity.setIdTime(userId, helper.getCurrentTime());
                            configEntity.setNDeleteAfter(7L);
                            configRepo.save(configEntity);
                        }
                        response.add(topic);
                    } else
                        log.info("Topic '" + topic + "' not found");
                }
            } else
                throw new RecordNotFoundException("Product not found");
        } else
            throw new RecordNotFoundException("Service mismatch! Only '"+ FIREBASE +"' is accepted");
        return response;
    }
}

