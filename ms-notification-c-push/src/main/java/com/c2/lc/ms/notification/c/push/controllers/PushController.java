package com.c2.lc.ms.notification.c.push.controllers;

import com.azure.messaging.eventhubs.EventHubClientBuilder;
import com.azure.messaging.eventhubs.EventProcessorClient;
import com.azure.messaging.eventhubs.EventProcessorClientBuilder;
import com.c2.lc.lib.controller.BaseController;
import com.c2.lc.ms.notification.c.push.bo.EmailModel;
import com.c2.lc.ms.notification.c.push.config.SampleCheckpointStore;
import com.c2.lc.ms.notification.c.push.services.interfaces.NotificationCPushDBService;
import com.c2.lc.ms.notification.c.push.transactions.interfaces.SendEmailTransaction;
import com.c2.lc.ms.notification.c.push.util.Constants;
import com.c2.lc.ms.notification.lib.entities.*;
import com.c2.lc.ms.notification.lib.repos.*;
import com.c2.lc.ms.notification.lib.services.interfaces.NotificationUserService;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.Objects;
import java.util.Optional;

import static com.c2.lc.ms.notification.c.push.util.Constants.FIREBASE;


@Log4j2
@RestController
public class PushController  extends BaseController {

    @Value("${eventhub.connection.string}")
    private String connectionString;

    @Value("${eventhub.name}")
    private String eventhubName;

    @Value("${telegram.chat.id}")
    private String chatId;

    @Value("${telegram.url.sendMessage}")
    private String telegramUrl;

    @Autowired private NotificationUserService userService;
    @Autowired private NotificationCPushDBService pushDBService;
    @Autowired private NotificationConfigRepo configRepo;
    @Autowired private NotificationTopicRepo notificationTopicRepo;
    @Autowired private NotificationProductRepo productRepo;
    @Autowired private NotificationCategoryRepo categoryRepo;
    @Autowired private NotificationUserRepo userRepo;

    @Autowired private SendEmailTransaction transaction;

    @PostConstruct
    public void eventHubListener()  throws IOException {
        EventProcessorClient eventProcessorClient = new EventProcessorClientBuilder()
                .consumerGroup(EventHubClientBuilder.DEFAULT_CONSUMER_GROUP_NAME)
                .connectionString(connectionString, eventhubName)
                .checkpointStore(new SampleCheckpointStore())
                .processEvent(eventContext -> {
                    log.debug("Partition id = " + eventContext.getPartitionContext().getPartitionId() + " and "
                            + "sequence number of event = " + eventContext.getEventData().getSequenceNumber());

                    try {
                        String consumedData = eventContext.getEventData().getBodyAsString();
                        log.debug("Raw data received from queue »  {} from partition {}",
                                consumedData, eventContext.getPartitionContext().getPartitionId());
                        JsonObject jsonObject = helper.fromJson(consumedData, JsonObject.class);

                        Optional<NotificationCategoryEntity> categoryEntity = categoryRepo.findByCategory(jsonObject.get("c_category").getAsString());
                        Optional<NotificationProductEntity> productEntity = productRepo.findById(jsonObject.get("c_product_key").getAsString());

                       // NotificationUserEntity userEntity = new NotificationUserEntity();
                        if (productEntity.isPresent() && categoryEntity.isPresent()) {

                            Optional<NotificationTopicEntity> topicEntity = notificationTopicRepo.findByTopic(jsonObject.get("c_topic").getAsString(), productEntity.get().getCProductKey());

                            if (topicEntity.isPresent()) {
                                NotificationConfigEntity configEntity = null;

                                if(jsonObject.has("j_service_name"))
                                {
                                JsonArray serviceArray = jsonObject.get("j_service_name").getAsJsonArray();
                                for (JsonElement serviceName : serviceArray) {

                                    String service = serviceName.getAsString();
                                    configEntity = configRepo.getByPK(jsonObject.get("c_customer_id").getAsString(), productEntity.get().getCProductKey(),
                                            topicEntity.get().getNTopicId(), service);

                                    if (configEntity != null) {
                                        if (Constants.TELEGRAM.equals(service)) {
                                            JsonObject jsonObject1 = new JsonObject();
                                            jsonObject1.addProperty("chat_id", chatId);
                                            jsonObject1.addProperty("parse_mode", "HTML");
                                            jsonObject1.addProperty("text", "<b>" + jsonObject.get("c_title").getAsString() + ": " + "</b>" + jsonObject.get("c_message").getAsString());
                                            pushDBService.sendTelegramNotification(telegramUrl, jsonObject1);

                                        }
                                        if (Constants.EMAIL.equals(service)) {
                                           /* transaction.sendMail();
                                        }*/
                                            if(productEntity.get().getCProductKey().equals("TS")) {
                                                    NotificationUserEntity entity = userRepo.getByc2code(configEntity.getCCustomerId(),Constants.EMAIL,productEntity.get().getCProductKey());
                                                    if (helper.isEmpty(entity)) {
                                                        EmailModel model = helper.fromJson(jsonObject, EmailModel.class);
                                                        transaction.sendMailBasedOnFrom(model);
                                                        NotificationUserEntity userEntity = userService.save(jsonObject, configEntity, categoryEntity);
                                                    } else
                                                        log.debug("Notify mail already sent to SalesTeam");
                                            } else {
                                                EmailModel model = helper.fromJson(jsonObject, EmailModel.class);
                                                transaction.sendMailBasedOnFrom(model);
                                            }
                                        }

                                    } else
                                        log.debug("User not subscribed for this service");
                                }
                            }
                                else
                                {
                                    configEntity = configRepo.getByPK(jsonObject.get("c_customer_id").getAsString(), productEntity.get().getCProductKey(),
                                            topicEntity.get().getNTopicId(), "FIREBASE");
                                    if (configEntity !=null) {
                                           NotificationUserEntity userEntity = userService.save(jsonObject, configEntity, categoryEntity);
                                            System.out.println(" ------ Push Notification Id: " + userEntity.getNNotificationId() + " ");
                                            pushDBService.pushNotification(userEntity);
                                    } else
                                        log.debug("Config not found with : " + ""+jsonObject.get("c_customer_id").getAsString()+"" +
                                                ":"+productEntity.get().getCProductKey()+":"+topicEntity.get().getNTopicId()+":FIREBASE ");
                                }
                            } else
                                log.debug("Topic not found");
                        } else
                            log.debug("Product or category not found");

                    } catch (Exception e) {

                        log.error("error occurred while processing the message{}", e.getMessage());
                        e.printStackTrace();
                    }
                })
                .processError(errorContext -> {
                    log.error("Error occurred while processing events " + errorContext.getThrowable());
                })
                .buildEventProcessorClient();
        eventProcessorClient.start();
    }

}
