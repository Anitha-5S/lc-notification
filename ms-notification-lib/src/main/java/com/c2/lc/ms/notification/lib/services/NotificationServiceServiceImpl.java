package com.c2.lc.ms.notification.lib.services;


import com.c2.lc.lib.exceptions.DuplicateRecordException;
import com.c2.lc.lib.exceptions.RecordNotFoundException;
import com.c2.lc.lib.services.BaseDBServiceImpl;
import com.c2.lc.lib.utils.Constants;
import com.c2.lc.ms.notification.lib.entities.NotificationProductEntity;
import com.c2.lc.ms.notification.lib.entities.NotificationServiceEntity;
import com.c2.lc.ms.notification.lib.repos.NotificationServiceRepo;
import com.c2.lc.ms.notification.lib.services.interfaces.NotificationServiceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class NotificationServiceServiceImpl extends BaseDBServiceImpl implements NotificationServiceService {

    @Autowired private NotificationServiceRepo notificationServiceRepo;

    @Override
    public List<NotificationServiceEntity> listService() throws RecordNotFoundException {
        List<NotificationServiceEntity> serviceList = notificationServiceRepo.getAllActiveService();
        if (serviceList.isEmpty()) {
            throw new RecordNotFoundException("Record not found");
        }
        return serviceList;
    }

    @Override
    public void deleteService(Long userId, String serviceName) throws RecordNotFoundException {
        Optional<NotificationServiceEntity> serviceEntity = notificationServiceRepo.findById(serviceName);
        if (serviceEntity.isPresent()) {
            // c_service_id not created
            NotificationServiceEntity entity = serviceEntity.get();
            entity.setCStatus(Constants.STATUS_INACTIVE);
            entity.setLastUpdated(userId, helper.getCurrentTime());
            notificationServiceRepo.save(entity);
        } else {
            throw new RecordNotFoundException("Record not found");
        }
    }

    @Override
    public void addService(Long userId, String serviceName, String endPoint, String description) throws DuplicateRecordException {
        Optional<NotificationServiceEntity> entity = notificationServiceRepo.findById(serviceName);
        if(entity.isPresent())
            throw new DuplicateRecordException("ServiceName already exists!");
        else {
            NotificationServiceEntity serviceEntity = new NotificationServiceEntity();
            serviceEntity.setCServiceName(serviceName);
            serviceEntity.setCEndPoint(endPoint);
            serviceEntity.setCDescription(description);
            serviceEntity.setIdTime(userId, helper.getCurrentTime());
            notificationServiceRepo.save(serviceEntity);
        }
    }
}
