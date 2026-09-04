package com.c2.lc.ms.notification.c.push.bo;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class AttachMentModel {

    @SerializedName("c_file_name")
    private String fileName;

    @SerializedName("c_file_data")
    private String fileData;

}
