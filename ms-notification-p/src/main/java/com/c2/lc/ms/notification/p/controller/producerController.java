package com.c2.lc.ms.notification.p.controller;

import com.azure.messaging.eventhubs.EventHubClientBuilder;
import com.azure.messaging.eventhubs.EventProcessorClient;
import com.azure.messaging.eventhubs.EventProcessorClientBuilder;
import com.c2.lc.lib.api.ApiResponse;
import com.c2.lc.lib.bo.LcHeaderBO;
import com.c2.lc.lib.controller.BaseController;
import com.c2.lc.lib.utils.Constants;
import com.c2.lc.ms.notification.lib.entities.NotificationUserEntity;
import com.c2.lc.ms.notification.p.transactions.interfaces.NotificationTransaction;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Payload;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Log4j2
@RestController
@RequestMapping("${api.base.path}/notification/p")
public class producerController extends BaseController {

    @Autowired
    private NotificationTransaction notificationTransaction;

    @Value("${api.base.path}")
    private String basePath;

    @Value("${eventhub.connection.string}")
    private String connectionString;

    @Value("${eventhub.name}")
    private String eventhubName;

    @PostMapping(value = "/notification", produces = Constants.APPLICATION_JSON_CHARSET_UTF_8, consumes = Constants.APPLICATION_JSON_CHARSET_UTF_8)
    public ResponseEntity<ApiResponse> notification(@RequestBody String payload) {
        ApiResponse apiResponse = this.initializeResponse(basePath +"/notification" + payload) ;

        try {
            JsonObject jsonObject = helper.fromJson(payload, JsonObject.class);
            log.info("Notification-p started at: " + LocalDateTime.now());
            notificationTransaction.notificationEventHub(jsonObject.get("data").getAsJsonArray());
            this.addMessage(apiResponse, "Message sent successfully!");

        } catch (Exception e) {
            this.handleAppExceptions(e, apiResponse);
        }
        return this.getResponseEntity(apiResponse);
    }


}
