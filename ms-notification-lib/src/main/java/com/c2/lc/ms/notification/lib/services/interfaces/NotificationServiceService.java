package com.c2.lc.ms.notification.lib.services.interfaces;


import com.c2.lc.lib.exceptions.DuplicateRecordException;
import com.c2.lc.lib.exceptions.RecordNotFoundException;
import com.c2.lc.lib.services.interfaces.BaseDBService;
import com.c2.lc.ms.notification.lib.entities.NotificationServiceEntity;

import java.util.List;

public interface NotificationServiceService extends BaseDBService {

    List<NotificationServiceEntity> listService() throws RecordNotFoundException;

    void deleteService(Long userId, String entity) throws RecordNotFoundException;

    void addService(Long userId, String serviceName, String endPoint, String description) throws DuplicateRecordException;
}
