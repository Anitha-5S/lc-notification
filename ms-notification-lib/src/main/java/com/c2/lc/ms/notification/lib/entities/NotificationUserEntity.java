package com.c2.lc.ms.notification.lib.entities;

import com.c2.lc.lib.utils.Constants;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import lombok.*;

import java.io.Serial;
import java.io.Serializable;
import javax.persistence.*;
import java.sql.Timestamp;
import java.time.LocalDateTime;


@EqualsAndHashCode(callSuper = false)
@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name="notification_user")
@NamedQuery(name="NotificationUserEntity.findAll", query="SELECT n FROM NotificationUserEntity n")
public class NotificationUserEntity implements Serializable {
	@Serial
	private static final long serialVersionUID = 1L;

	@SerializedName("n_notification_id")
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="n_notification_id", unique=true, nullable=false)
	private Long nNotificationId;

	@SerializedName("c_from")
	@Column(name="c_from", length=32)
	private String cFrom;

	@SerializedName("c_message")
	@Column(name="c_message", nullable=false, length=256)
	private String cMessage;

	@SerializedName("c_options")
	@Column(name="c_options", nullable=false, length=1024)
	private String cOptions;

	@SerializedName("c_title")
	@Column(name="c_title", length=128)
	private String cTitle;

	@SerializedName("c_customer_id")
	@Column(name="c_customer_id",nullable=false, length = 32)
	private String cCustomerId;

	@SerializedName("t_delivered_at")
	@Column(name="t_delivered_at")
	private LocalDateTime tDeliveredAt;

	@SerializedName("t_expires_at")
	@Column(name="t_expires_at")
	private LocalDateTime tExpiresAt;

	@SerializedName("t_generated_at")
	@Column(name="t_generated_at")
	private LocalDateTime tGeneratedAt;

	@SerializedName("t_read_at")
	@Column(name="t_read_at")
	private LocalDateTime tReadAt;

	@SerializedName("c_status")
	@Column(name="c_status", nullable=false, length=2)
	private String cStatus = Constants.STATUS_ACTIVE;

	@SerializedName("c_action_id")
	@Column(name = "c_action_id", nullable = false,length = 16)
	private String cActionId;

	//bi-directional many-to-one association to NotificationCategoryEntity
	@Expose(serialize = false)
	@ManyToOne
	@JoinColumn(name="n_category_id")
	private NotificationCategoryEntity notificationCategory;

	//bi-directional many-to-one association to NotificationProductEntity
	@Expose(serialize = false)
	@ManyToOne
	@JoinColumn(name="c_product_key")
	private NotificationProductEntity notificationProduct;

	//bi-directional many-to-one association to NotificationTopicEntity
	@Expose(serialize = false)
	@ManyToOne
	@JoinColumn(name="n_topic_id")
	private NotificationTopicEntity notificationTopic;

	public NotificationCategoryEntity getNotificationCategory() {
		return this.notificationCategory;
	}

	public void setNotificationCategory(NotificationCategoryEntity notificationCategory) {
		this.notificationCategory = notificationCategory;
	}

	public NotificationProductEntity getNotificationProduct() {
		return this.notificationProduct;
	}

	public void setNotificationProduct(NotificationProductEntity notificationProduct) {
		this.notificationProduct = notificationProduct;
	}

	public NotificationTopicEntity getNotificationTopic() {
		return this.notificationTopic;
	}

	public void setNotificationTopic(NotificationTopicEntity notificationTopic) {
		this.notificationTopic = notificationTopic;
	}
}