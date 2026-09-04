package com.c2.lc.ms.notification.lib.services.interfaces;


import com.c2.lc.lib.exceptions.RecordNotFoundException;
import com.c2.lc.lib.services.interfaces.BaseDBService;
import com.c2.lc.ms.notification.lib.entities.NotificationTopicEntity;
import com.google.gson.JsonArray;

import java.util.List;

public interface NotificationTopicService extends BaseDBService {

    List<NotificationTopicEntity> list(String productKey) throws RecordNotFoundException;

    void delete(Long userId,  Long topicId) throws RecordNotFoundException;

    List<String> add(Long userId, JsonArray topics, String productKey) throws RecordNotFoundException;
}
