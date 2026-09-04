package com.c2.lc.ms.notification.lib.services.interfaces;


import com.c2.lc.lib.exceptions.DuplicateRecordException;
import com.c2.lc.lib.exceptions.RecordNotFoundException;
import com.c2.lc.lib.services.interfaces.BaseDBService;
import com.c2.lc.ms.notification.lib.entities.NotificationProductEntity;

import java.util.List;

public interface NotificationProductService extends BaseDBService {

    void add(Long userId, String productKey, String productName) throws DuplicateRecordException;

    List<NotificationProductEntity> list() throws RecordNotFoundException;

    void deleteProduct(Long userId, String productKey) throws RecordNotFoundException;
}
