package com.c2.lc.ms.notification.lib.repos;

import com.c2.lc.ms.notification.lib.entities.NotificationConfigEntity;
import com.c2.lc.ms.notification.lib.entities.NotificationConfigPK;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface NotificationConfigRepo extends JpaRepository<NotificationConfigEntity, NotificationConfigPK>, JpaSpecificationExecutor<NotificationConfigEntity> {

    @Query("SELECT n FROM NotificationConfigEntity n WHERE n.cStatus = :status AND n.cCustomerId = :customerId ORDER BY n.cCustomerId ASC, n.cProductKey ASC," +
            "n.nTopicId ASC, n.cServiceName ASC ")
    List<NotificationConfigEntity> getConfigList(String status, String customerId);

    @Query(value = "SELECT * FROM notification_config nc " +
            "WHERE c_customer_id = :customerId AND c_product_key = :cProductKey AND n_topic_id = :cTopic AND c_service_name = :cServiceName " +
            "ORDER BY c_customer_id ASC ",nativeQuery = true)
    NotificationConfigEntity getByPK(String customerId, String cProductKey, Long cTopic, String cServiceName);

    @Query(value = "SELECT * FROM notification_config nc " +
            "WHERE c_customer_id = :userId AND c_product_key = :productKey AND c_service_name = :serviceName AND n_topic_id IN :topicList " +
            "ORDER BY c_customer_id ASC ",nativeQuery = true)
    List<NotificationConfigEntity> findByPk(String userId, List<Long> topicList, String productKey, String serviceName);
}