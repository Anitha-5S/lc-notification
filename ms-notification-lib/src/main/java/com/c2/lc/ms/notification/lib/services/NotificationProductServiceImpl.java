package com.c2.lc.ms.notification.lib.services;


import com.c2.lc.lib.exceptions.DuplicateRecordException;
import com.c2.lc.lib.exceptions.RecordNotFoundException;
import com.c2.lc.lib.services.BaseDBServiceImpl;
import com.c2.lc.lib.utils.Constants;
import com.c2.lc.ms.notification.lib.entities.NotificationProductEntity;
import com.c2.lc.ms.notification.lib.repos.NotificationProductRepo;
import com.c2.lc.ms.notification.lib.services.interfaces.NotificationProductService;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.apache.kafka.common.errors.InvalidRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.Query;
import java.util.List;
import java.util.Optional;

@Service
public class NotificationProductServiceImpl extends BaseDBServiceImpl implements NotificationProductService {

    @Autowired NotificationProductRepo notificationProductRepo;

    @Override
    public void add(Long userId, String productKey, String productName) throws DuplicateRecordException {
        Optional<NotificationProductEntity> productEntity = notificationProductRepo.findById(productKey);
        if (productEntity.isPresent()) {
            throw new DuplicateRecordException("Product Already Added");
        }
        NotificationProductEntity entity = new NotificationProductEntity();
        entity.setCProductKey(productKey);
        entity.setCProductName(productName);
        entity.setIdTime(userId, helper.getCurrentTime());
        notificationProductRepo.save(entity);
    }

    @Override
    public List<NotificationProductEntity> list() throws RecordNotFoundException {
        List<NotificationProductEntity> productList = notificationProductRepo.getAllActiveProduct();
        if (productList.isEmpty()) {
            throw new RecordNotFoundException("Record not found");
        }
        return productList;
    }

    @Override
    public void deleteProduct(Long userId, String productKey) throws RecordNotFoundException {
        Optional<NotificationProductEntity> productEntity = notificationProductRepo.findById(productKey);
        if (productEntity.isPresent()) {
            if (productEntity.get().getCStatus().equals(Constants.STATUS_INACTIVE)) {
                throw new InvalidRequestException("Product Is Already InActive");
            }
            NotificationProductEntity entity = productEntity.get();
            entity.setLastUpdated(userId, helper.getCurrentTime());
            entity.setCStatus(Constants.STATUS_INACTIVE);
            notificationProductRepo.save(entity);
        } else {
            throw new RecordNotFoundException("Record not found");
        }
    }
}
