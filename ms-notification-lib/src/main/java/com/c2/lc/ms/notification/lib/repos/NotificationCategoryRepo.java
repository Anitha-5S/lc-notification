package com.c2.lc.ms.notification.lib.repos;

import com.c2.lc.ms.notification.lib.entities.NotificationCategoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository("NotificationCategoryRepository")
public interface NotificationCategoryRepo extends JpaRepository<NotificationCategoryEntity, Long>, JpaSpecificationExecutor<NotificationCategoryEntity> {

    @Query("SELECT n FROM NotificationCategoryEntity n WHERE n.cStatus = 'A' ORDER BY n.cCategory ASC ")
    List<NotificationCategoryEntity> getAllActiveCategory();

    @Query(value = "SELECT * FROM notification_category WHERE c_category =:category AND c_status = 'A' ",nativeQuery = true)
    Optional<NotificationCategoryEntity> findByCategory(String category);
}