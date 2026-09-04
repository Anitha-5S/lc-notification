package com.c2.lc.ms.notification.lib.entities;

import com.c2.lc.lib.db.DateAudit;
import com.c2.lc.lib.utils.Constants;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import lombok.*;

import java.io.Serial;
import java.io.Serializable;
import javax.persistence.*;
import java.sql.Timestamp;
import java.util.List;


@EqualsAndHashCode(callSuper = false)
@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name="notification_topic")
@NamedQuery(name="NotificationTopicEntity.findAll", query="SELECT n FROM NotificationTopicEntity n")
public class NotificationTopicEntity extends DateAudit implements Serializable {

	@Serial
	private static final long serialVersionUID = 1L;

	@SerializedName("n_topic_id")
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="n_topic_id", unique=true, nullable=false)
	private Long nTopicId;

	@SerializedName("c_status")
	@Column(name="c_status", nullable=false, length=2)
	private String cStatus = Constants.STATUS_ACTIVE;

	@SerializedName("c_topic")
	@Column(name="c_topic", length=128)
	private String cTopic;

	//bi-directional many-to-one association to NotificationConfigEntity
	@SerializedName("notificationConfigs")
	@OneToMany(mappedBy="notificationTopic",cascade = CascadeType.ALL)
	private List<NotificationConfigEntity> notificationConfigs;

	//bi-directional many-to-one association to NotificationProductEntity
	@Expose(serialize = false)
	@ManyToOne
	@JoinColumn(name="c_product_key", nullable=false)
	private NotificationProductEntity notificationProduct;

	//bi-directional many-to-one association to NotificationUserEntity
	@SerializedName("notificationUsers")
	@OneToMany(mappedBy="notificationTopic", cascade = CascadeType.ALL)
	private List<NotificationUserEntity> notificationUsers;

	public List<NotificationConfigEntity> getNotificationConfigs() {
		return this.notificationConfigs;
	}

	public void setNotificationConfigs(List<NotificationConfigEntity> notificationConfigs) {
		this.notificationConfigs = notificationConfigs;
	}

	public NotificationConfigEntity addNotificationConfig(NotificationConfigEntity notificationConfig) {
		getNotificationConfigs().add(notificationConfig);
		notificationConfig.setNotificationTopic(this);

		return notificationConfig;
	}

	public NotificationConfigEntity removeNotificationConfig(NotificationConfigEntity notificationConfig) {
		getNotificationConfigs().remove(notificationConfig);
		notificationConfig.setNotificationTopic(null);

		return notificationConfig;
	}

	public NotificationProductEntity getNotificationProduct() {
		return this.notificationProduct;
	}

	public void setNotificationProduct(NotificationProductEntity notificationProduct) {
		this.notificationProduct = notificationProduct;
	}

	public List<NotificationUserEntity> getNotificationUsers() {
		return this.notificationUsers;
	}

	public void setNotificationUsers(List<NotificationUserEntity> notificationUsers) {
		this.notificationUsers = notificationUsers;
	}

	public NotificationUserEntity addNotificationUser(NotificationUserEntity notificationUser) {
		getNotificationUsers().add(notificationUser);
		notificationUser.setNotificationTopic(this);

		return notificationUser;
	}

	public NotificationUserEntity removeNotificationUser(NotificationUserEntity notificationUser) {
		getNotificationUsers().remove(notificationUser);
		notificationUser.setNotificationTopic(null);

		return notificationUser;
	}

}