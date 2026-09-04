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


@EqualsAndHashCode(callSuper = false)
@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name="notification_service")
@NamedQuery(name="NotificationServiceEntity.findAll", query="SELECT n FROM NotificationServiceEntity n")
public class NotificationServiceEntity extends DateAudit implements Serializable {
	@Serial
	private static final long serialVersionUID = 1L;

	@SerializedName("c_service_name")
	@Id
	//@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="c_service_name", unique=true, nullable=false, length=128)
	private String cServiceName;

	@SerializedName("c_description")
	@Column(name="c_description", length=1024)
	private String cDescription;

	@SerializedName("c_end_point")
	@Column(name="c_end_point", length=1024)
	private String cEndPoint;

	@SerializedName("c_status")
	@Column(name="c_status", nullable=false, length=2)
	private String cStatus = Constants.STATUS_ACTIVE;

	//bi-directional many-to-one association to NotificationConfigEntity
	@SerializedName("notificationConfigs")
	@OneToMany(mappedBy="notificationService",cascade = CascadeType.ALL)
	private List<NotificationConfigEntity> notificationConfigs;

	public List<NotificationConfigEntity> getNotificationConfigs() {
		return this.notificationConfigs;
	}

	public void setNotificationConfigs(List<NotificationConfigEntity> notificationConfigs) {
		this.notificationConfigs = notificationConfigs;
	}

	public NotificationConfigEntity addNotificationConfig(NotificationConfigEntity notificationConfig) {
		getNotificationConfigs().add(notificationConfig);
		notificationConfig.setNotificationService(this);

		return notificationConfig;
	}

	public NotificationConfigEntity removeNotificationConfig(NotificationConfigEntity notificationConfig) {
		getNotificationConfigs().remove(notificationConfig);
		notificationConfig.setNotificationService(null);

		return notificationConfig;
	}

}