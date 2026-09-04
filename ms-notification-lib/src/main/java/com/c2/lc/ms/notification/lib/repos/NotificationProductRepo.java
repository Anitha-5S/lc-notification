package com.c2.lc.ms.notification.lib.repos;

import com.c2.lc.ms.notification.lib.entities.NotificationProductEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationProductRepo extends JpaRepository<NotificationProductEntity, String>, JpaSpecificationExecutor<NotificationProductEntity> {

    @Query("SELECT n FROM NotificationProductEntity n WHERE n.cStatus = 'A' ORDER BY n.cProductName ASC")
    List<NotificationProductEntity> getAllActiveProduct();

}