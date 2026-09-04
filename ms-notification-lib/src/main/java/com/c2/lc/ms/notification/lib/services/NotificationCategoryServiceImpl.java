package com.c2.lc.ms.notification.lib.services;


import com.c2.lc.lib.exceptions.DuplicateRecordException;
import com.c2.lc.lib.exceptions.RecordNotFoundException;
import com.c2.lc.lib.services.BaseDBServiceImpl;
import com.c2.lc.lib.utils.Constants;
import com.c2.lc.ms.notification.lib.entities.NotificationCategoryEntity;
import com.c2.lc.ms.notification.lib.repos.NotificationCategoryRepo;
import com.c2.lc.ms.notification.lib.services.interfaces.NotificationCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class NotificationCategoryServiceImpl extends BaseDBServiceImpl implements NotificationCategoryService {

    @Autowired private NotificationCategoryRepo notificationCategoryRepo;

    @Override
    public void delete(Long userId, Long categoryId) throws RecordNotFoundException {
        Optional<NotificationCategoryEntity> entity = notificationCategoryRepo.findById(categoryId);
        if (entity.isEmpty()) {
            throw new RecordNotFoundException("Record not found");
        } else {
            NotificationCategoryEntity categoryEntity = entity.get();
            categoryEntity.setLastUpdated(userId, helper.getCurrentTime());
            categoryEntity.setCStatus(Constants.STATUS_INACTIVE);
            notificationCategoryRepo.save(categoryEntity);
        }
    }

    @Override
    public List<NotificationCategoryEntity> list() throws RecordNotFoundException {
        List<NotificationCategoryEntity> categoryList = notificationCategoryRepo.getAllActiveCategory();
        if (categoryList.isEmpty()) {
            throw new RecordNotFoundException("Record not found");
        }
        return categoryList;
    }

    @Override
    public void add(Long userId, String category) throws DuplicateRecordException {
        Optional<NotificationCategoryEntity> entity = notificationCategoryRepo.findByCategory(category);
        if(entity.isPresent()){
            throw new DuplicateRecordException("Record already exists!");
        }
        NotificationCategoryEntity categoryEntity = new NotificationCategoryEntity();
        categoryEntity.setCCategory(category);
        categoryEntity.setIdTime(userId, helper.getCurrentTime());
        notificationCategoryRepo.save(categoryEntity);
    }
}
