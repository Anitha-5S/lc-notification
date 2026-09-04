package com.c2.lc.ms.notification.lib.entities;

import java.io.Serial;
import java.io.Serializable;
import javax.persistence.*;

public class NotificationConfigPK implements Serializable {
	//default serial version id, required for serializable classes.
	@Serial
	private static final long serialVersionUID = 1L;

	@Column(name="c_customer_id", unique=true, nullable=false, length=32)
	private String cCustomerId;

	@Column(name="c_product_key", insertable=false, updatable=false, unique=true, nullable=false, length=32)
	private String cProductKey;

	@Column(name="n_topic_id", insertable=false, updatable=false, unique=true, nullable=false)
	private Long nTopicId;

	@Column(name="c_service_name", insertable = false, updatable = false,unique=true, nullable=false, length=128)
	private String cServiceName;

	public NotificationConfigPK() {
	}
	public String getCCustomerId() {
		return this.cCustomerId;
	}

	public void setCCustomerId(String cCustomerId) {
		this.cCustomerId = cCustomerId;
	}

	public String getCProductKey() {
		return this.cProductKey;
	}

	public void setCProductKey(String cProductKey) {
		this.cProductKey = cProductKey;
	}

	public Long getNTopicId() {
		return this.nTopicId;
	}

	public void setNTopicId(Long nTopicId) {
		this.nTopicId = nTopicId;
	}

	public String getCServiceName() {return cServiceName;}

	public void setCServiceName(String cServiceName ) {this.cServiceName = cServiceName;}

	public boolean equals(Object other) {
		if (this == other) {
			return true;
		}
		if (!(other instanceof NotificationConfigPK)) {
			return false;
		}
		NotificationConfigPK castOther = (NotificationConfigPK)other;
		return 
			this.cCustomerId.equals(castOther.cCustomerId)
			&& this.cProductKey.equals(castOther.cProductKey)
			&& this.nTopicId.equals(castOther.nTopicId);
	}

	public int hashCode() {
		final int prime = 31;
		int hash = 17;
		hash = hash * prime + this.cCustomerId.hashCode();
		hash = hash * prime + this.cProductKey.hashCode();
		hash = hash * prime + this.nTopicId.hashCode();
		
		return hash;
	}
}