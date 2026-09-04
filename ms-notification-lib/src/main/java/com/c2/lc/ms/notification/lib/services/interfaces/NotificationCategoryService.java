package com.c2.lc.ms.notification.lib.services.interfaces;


import com.c2.lc.lib.exceptions.DuplicateRecordException;
import com.c2.lc.lib.exceptions.RecordNotFoundException;
import com.c2.lc.lib.services.interfaces.BaseDBService;
import com.c2.lc.ms.notification.lib.entities.NotificationCategoryEntity;

import java.util.List;

public interface NotificationCategoryService extends BaseDBService {

    void add(Long userId, String entity) throws DuplicateRecordException;

    void delete(Long userId, Long entity) throws RecordNotFoundException;

    List<NotificationCategoryEntity> list() throws RecordNotFoundException;
}
