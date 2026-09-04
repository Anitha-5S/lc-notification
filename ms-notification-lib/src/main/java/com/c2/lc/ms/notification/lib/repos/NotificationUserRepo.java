package com.c2.lc.ms.notification.lib.repos;

import com.c2.lc.ms.notification.lib.entities.NotificationUserEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationUserRepo extends JpaRepository<NotificationUserEntity, Long>, JpaSpecificationExecutor<NotificationUserEntity> {

    @Query(value = "SELECT * FROM notification_user nu WHERE  c_status = 'A' AND t_read_at IS NULL AND c_customer_id= :userId ORDER BY t_generated_at DESC ",nativeQuery = true)
    List<NotificationUserEntity> getUnreadNotifications(String userId, Pageable pageable);

    @Query(value = "SELECT COUNT(*) FROM notification_user nu WHERE c_status = 'A' " +
            "AND t_read_at IS NULL AND c_customer_id= :userId " +
            "AND c_product_key = :product_key ",nativeQuery = true)
    long unreadNotificationsCount(String product_key, String userId);

    @Query(value = "SELECT nu.n_notification_id,nc2.c_customer_id ,  " +
            "nu.c_product_key, np.c_product_name, nc.c_category , nt.c_topic,  " +
            "nu.c_title, nu.c_from, nu.c_message,nu.c_action_id, nu.t_generated_at, nu.t_read_at, nu.t_delivered_at, nu.c_options, nu.c_status   " +
            "FROM notification_user nu  " +
            "JOIN notification_topic nt on nu.n_topic_id = nt.n_topic_id and nt.c_product_key = nu.c_product_key   " +
            "JOIN notification_category nc on nu.n_category_id = nc.n_category_id   " +
            "JOIN notification_product np on nu.c_product_key = np.c_product_key   " +
            "JOIN notification_config nc2 on nu.c_customer_id = nc2.c_customer_id and nc2.n_topic_id = nt.n_topic_id   " +
            "WHERE nu.c_status = 'A' AND nu.c_customer_id = :userId  " +
            "ORDER BY t_generated_at DESC ",nativeQuery = true)
    List<String> getAllNotifications(String userId, Pageable pageable);

    @Query(value = "SELECT * FROM notification_user nu WHERE t_expires_at < now() " +
            " AND c_customer_id = :customerID AND c_status = 'A' " +
            " ORDER BY t_expires_at ASC ",nativeQuery = true)
    List<NotificationUserEntity> deleteExpired(String customerID);

    @Query(value = "SELECT * FROM notification_user nu " +
            "JOIN notification_config nc on nu.c_customer_id = nc.c_customer_id  and nc.c_product_key = nu.c_product_key " +
            "WHERE nu.c_customer_id = :customerID AND nu.c_status = 'A' AND nc.c_service_name = :email and nu.c_product_key = :cProductKey " +
            "and nu.c_from ='noreply.liveconnect@csquare.in' " +
            "ORDER BY t_generated_at ASC",nativeQuery = true)
    NotificationUserEntity getByc2code(String customerID, String email, String cProductKey);

    @Query(value = "SELECT * FROM notification_user nu WHERE c_status = 'A' AND c_customer_id = :userId AND c_product_key = :product_key ORDER BY t_generated_at DESC ",nativeQuery = true)
    List<NotificationUserEntity> getUserNotifications(String userId, String product_key);

    @Query(value = "SELECT * FROM notification_user nu WHERE c_status = 'A'" +
            " AND c_customer_id = :userId AND c_product_key = :product_key AND n_notification_id = :notificationId ORDER BY t_generated_at DESC ",nativeQuery = true)
    NotificationUserEntity findByUserId(String userId, Long notificationId, String product_key);

}