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



@EqualsAndHashCode(callSuper = false)
@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name="notification_config")
@IdClass(value = NotificationConfigPK.class )
@NamedQuery(name="NotificationConfigEntity.findAll", query="SELECT n FROM NotificationConfigEntity n")
public class NotificationConfigEntity extends DateAudit implements Serializable {
	@Serial
	private static final long serialVersionUID = 1L;

	/*@EmbeddedId
	private NotificationConfigPK id;*/

	@SerializedName("c_customer_id")
	@Id
	@Column(name="c_customer_id", unique=true, nullable=false, length=32)
	private String cCustomerId;

	@SerializedName("c_product_key")
	@Id
	@Column(name="c_product_key", updatable=false, unique=true, nullable=false, length=32)
	private String cProductKey;

	@SerializedName("n_topic_id")
	@Id
	@Column(name="n_topic_id", insertable=false, updatable=false, unique=true, nullable=false)
	private Long nTopicId;

	@SerializedName("c_service_name")
	@Id
	@Column(name="c_service_name", insertable = false, updatable = false,unique=true, nullable=false, length=128)
	private String cServiceName;

	@SerializedName("c_service_options")
	@Column(name="c_service_options", length=1024)
	private String cServiceOptions;

	@SerializedName("c_status")
	@Column(name="c_status", length=2)
	private String cStatus = Constants.STATUS_ACTIVE;

	@SerializedName("n_delete_after")
	@Column(name="n_delete_after")
	private Long nDeleteAfter;

	//bi-directional many-to-one association to NotificationProductEntity
	@Expose(serialize = false)
	@ManyToOne
	@JoinColumn(name="c_product_key", nullable=false, insertable=false, updatable=false)
	private NotificationProductEntity notificationProduct;

	//bi-directional many-to-one association to NotificationTopicEntity
	@Expose(serialize = false)
	@ManyToOne
	@JoinColumn(name="n_topic_id", nullable=false, insertable=false, updatable=false)
	private NotificationTopicEntity notificationTopic;

	//bi-directional many-to-one association to NotificationProductEntity
	@Expose(serialize = false)
	@ManyToOne
	@JoinColumn(name="c_service_name", nullable=false, insertable=false, updatable=false)
	private NotificationServiceEntity notificationService;

}