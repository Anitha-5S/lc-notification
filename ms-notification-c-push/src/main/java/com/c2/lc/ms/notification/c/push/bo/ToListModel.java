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
@AllArgsConstructor
@NoArgsConstructor
public class ToListModel {

    @SerializedName("c_to")
    private List<String> to;

    @SerializedName("c_to_cc")
    private List<String> toCc;

    @SerializedName("c_to_bcc")
    private List<String> toBcc;

}
