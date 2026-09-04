package com.c2.lc.ms.notification.lib.entities;

import com.c2.lc.lib.db.DateAudit;
import com.c2.lc.lib.utils.Constants;
import com.google.gson.annotations.SerializedName;
import lombok.*;

import java.io.Serial;
import java.io.Serializable;
import javax.persistence.*;
import java.sql.Timestamp;
import java.util.List;


@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name="notification_product")
@NamedQuery(name="NotificationProductEntity.findAll", query="SELECT n FROM NotificationProductEntity n")
public class NotificationProductEntity extends DateAudit implements Serializable {
	@Serial
	private static final long serialVersionUID = 1L;

	@SerializedName("c_product_key")
	@Id
	//@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="c_product_key", unique=true, nullable=false, length=32)
	private String cProductKey;

	@SerializedName("c_product_name")
	@Column(name="c_product_name", nullable=false, length=128)
	private String cProductName;

	@SerializedName("c_status")
	@Column(name="c_status", nullable=false, length=2)
	private String cStatus = Constants.STATUS_ACTIVE;

	//bi-directional many-to-one association to NotificationConfigEntity
	@SerializedName("notificationConfigs")
	@OneToMany(cascade = CascadeType.ALL ,mappedBy="notificationProduct")
	private List<NotificationConfigEntity> notificationConfigs;

	//bi-directional many-to-one association to NotificationTopicEntity
	@SerializedName("notificationTopics")
	@OneToMany(cascade = CascadeType.ALL,mappedBy="notificationProduct")
	private List<NotificationTopicEntity> notificationTopics;

	//bi-directional many-to-one association to NotificationUserEntity
	@SerializedName("notificationUsers")
	@OneToMany(cascade = CascadeType.ALL,mappedBy="notificationProduct")
	private List<NotificationUserEntity> notificationUsers;

	public List<NotificationConfigEntity> getNotificationConfigs() {
		return this.notificationConfigs;
	}

	public void setNotificationConfigs(List<NotificationConfigEntity> notificationConfigs) {
		this.notificationConfigs = notificationConfigs;
	}

	public NotificationConfigEntity addNotificationConfig(NotificationConfigEntity notificationConfig) {
		getNotificationConfigs().add(notificationConfig);
		notificationConfig.setNotificationProduct(this);

		return notificationConfig;
	}

	public NotificationConfigEntity removeNotificationConfig(NotificationConfigEntity notificationConfig) {
		getNotificationConfigs().remove(notificationConfig);
		notificationConfig.setNotificationProduct(null);

		return notificationConfig;
	}

	public List<NotificationTopicEntity> getNotificationTopics() {
		return this.notificationTopics;
	}

	public void setNotificationTopics(List<NotificationTopicEntity> notificationTopics) {
		this.notificationTopics = notificationTopics;
	}

	public NotificationTopicEntity addNotificationTopic(NotificationTopicEntity notificationTopic) {
		getNotificationTopics().add(notificationTopic);
		notificationTopic.setNotificationProduct(this);

		return notificationTopic;
	}

	public NotificationTopicEntity removeNotificationTopic(NotificationTopicEntity notificationTopic) {
		getNotificationTopics().remove(notificationTopic);
		notificationTopic.setNotificationProduct(null);

		return notificationTopic;
	}

	public List<NotificationUserEntity> getNotificationUsers() {
		return this.notificationUsers;
	}

	public void setNotificationUsers(List<NotificationUserEntity> notificationUsers) {
		this.notificationUsers = notificationUsers;
	}

	public NotificationUserEntity addNotificationUser(NotificationUserEntity notificationUser) {
		getNotificationUsers().add(notificationUser);
		notificationUser.setNotificationProduct(this);

		return notificationUser;
	}

	public NotificationUserEntity removeNotificationUser(NotificationUserEntity notificationUser) {
		getNotificationUsers().remove(notificationUser);
		notificationUser.setNotificationProduct(null);

		return notificationUser;
	}

}