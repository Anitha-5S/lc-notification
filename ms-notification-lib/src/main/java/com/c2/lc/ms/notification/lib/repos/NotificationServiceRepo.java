package com.c2.lc.ms.notification.lib.repos;

import com.c2.lc.ms.notification.lib.entities.NotificationServiceEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface NotificationServiceRepo extends JpaRepository<NotificationServiceEntity, String>, JpaSpecificationExecutor<NotificationServiceEntity> {

    @Query("SELECT n FROM NotificationServiceEntity n WHERE n.cStatus = 'A' ORDER BY n.cServiceName ASC")
    List<NotificationServiceEntity> getAllActiveService();
}