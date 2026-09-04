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
@Table(name="notification_category")
@NamedQuery(name="NotificationCategoryEntity.findAll", query="SELECT n FROM NotificationCategoryEntity n")
public class NotificationCategoryEntity  extends DateAudit implements Serializable {
	@Serial
	private static final long serialVersionUID = 1L;

	@SerializedName(value = "n_category_id")
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="n_category_id", unique=true, nullable=false)
	private Long nCategoryId;

	@SerializedName(value = "c_category")
	@Column(name="c_category", length=32)
	private String cCategory;

	@SerializedName(value = "c_status")
	@Column(name="c_status", nullable=false, length=2)
	private String cStatus = Constants.STATUS_ACTIVE;

	//bi-directional many-to-one association to NotificationUserEntity
	@SerializedName(value = "notificationUsers")
	@OneToMany(mappedBy="notificationCategory", cascade = CascadeType.ALL)
	private List<NotificationUserEntity> notificationUsers;


	public List<NotificationUserEntity> getNotificationUsers() {
		return this.notificationUsers;
	}

	public void setNotificationUsers(List<NotificationUserEntity> notificationUsers) {
		this.notificationUsers = notificationUsers;
	}

	public NotificationUserEntity addNotificationUser(NotificationUserEntity notificationUser) {
		getNotificationUsers().add(notificationUser);
		notificationUser.setNotificationCategory(this);

		return notificationUser;
	}

	public NotificationUserEntity removeNotificationUser(NotificationUserEntity notificationUser) {
		getNotificationUsers().remove(notificationUser);
		notificationUser.setNotificationCategory(null);

		return notificationUser;
	}

}