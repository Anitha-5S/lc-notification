package com.c2.lc.ms.notification.lib.services.interfaces;


import com.c2.lc.lib.exceptions.RecordNotFoundException;
import com.c2.lc.lib.services.interfaces.BaseDBService;
import com.c2.lc.ms.notification.lib.entities.NotificationConfigEntity;
import com.google.gson.JsonArray;

import java.util.List;

public interface NotificationConfigService extends BaseDBService {

    List<NotificationConfigEntity> listConfig(String status, String customerId) throws RecordNotFoundException;

    void addConfig(Long userId, String customerId, String productKey, Long topicId, String serviceName, String serviceOptions, Long deleteAfter) throws Exception;

    void deleteConfig(Long userId, String productKey, String serviceName, String customerId, Long topicId) throws RecordNotFoundException;

    List<String> updateToken(Long userId, String productKey, String serviceName, JsonArray topics, String deviceToken) throws RecordNotFoundException;
}
