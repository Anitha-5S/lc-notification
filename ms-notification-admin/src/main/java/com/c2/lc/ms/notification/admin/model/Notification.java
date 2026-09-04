package com.c2.lc.ms.notification.admin.model;

import com.c2.lc.lib.utils.Constants;
import com.google.gson.annotations.SerializedName;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.time.LocalDateTime;

@Getter
@Setter
public class Notification {
    private Long n_notification_id;
    private String c_from;
    private String c_message;
    private String c_options;
    private String c_title;
    private String c_customer_id;
    private String c_product_key;
    private Long n_category_id;
    private Long n_topic_id;
    private LocalDateTime t_generated_at;

}
