package com.c2.lc.ms.notification.c.push.bo;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class EmailModel {

    @SerializedName("c_from")
    private String from;

    @SerializedName("to")
    private ToListModel toList;

    @SerializedName("c_subject")
    private String subject;

    @SerializedName("c_content")
    private String content;

    @SerializedName("attachMents")
    private List<AttachMentModel> attachments;
}
