package com.c2.lc.ms.notification.lib.repos;

import com.c2.lc.ms.notification.lib.entities.NotificationTopicEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface NotificationTopicRepo extends JpaRepository<NotificationTopicEntity, Long>, JpaSpecificationExecutor<NotificationTopicEntity> {

    @Query(value = "SELECT * FROM notification_topic nt " +
            "JOIN notification_product np ON nt.c_product_key = np.c_product_key " +
            "WHERE nt.c_status = 'A' AND np.c_product_key = :productKey ORDER BY c_topic ASC ",nativeQuery = true)
    List<NotificationTopicEntity> getAllTopicsByProduct(String productKey);

    @Query(value = "SELECT * FROM notification_topic nt " +
            "JOIN notification_product np ON nt.c_product_key = np.c_product_key " +
            "WHERE nt.c_status = 'A' AND nt.c_topic IN :topics AND np.c_product_key = :productKey ORDER BY c_topic ASC ",nativeQuery = true)
    List<NotificationTopicEntity> getTopicId(List<String> topics, String productKey);

    @Query(value = "SELECT * FROM notification_topic nt " +
            "JOIN notification_product np ON nt.c_product_key = np.c_product_key " +
            "WHERE nt.c_status = 'A' AND nt.c_topic = :cTopic AND np.c_product_key = :productKey ORDER BY c_topic ASC ",nativeQuery = true)
    Optional<NotificationTopicEntity> findByTopic(String cTopic, String productKey);

    @Query(value = "SELECT * FROM notification_topic nt " +
            "JOIN notification_product np ON nt.c_product_key = np.c_product_key " +
            "WHERE nt.c_status = 'A' AND nt.c_topic IN :topicList AND np.c_product_key = :productKey ORDER BY c_topic ASC ",nativeQuery = true)
    List<NotificationTopicEntity> findByTopicList(List<String> topicList, String productKey);
}