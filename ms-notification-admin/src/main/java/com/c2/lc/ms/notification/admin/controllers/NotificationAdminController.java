package com.c2.lc.ms.notification.admin.controllers;

import com.c2.lc.lib.api.ApiResponse;
import com.c2.lc.lib.bo.LcHeaderBO;
import com.c2.lc.lib.bo.PageBO;
import com.c2.lc.lib.controller.LoBaseController;
import com.c2.lc.lib.exceptions.InputPayloadException;
import com.c2.lc.lib.exceptions.InvalidRequestException;
import com.c2.lc.lib.exceptions.RecordNotFoundException;
import com.c2.lc.lib.utils.Constants;
import com.c2.lc.ms.notification.admin.transactions.interfaces.NotificationAdminTransaction;
import com.c2.lc.ms.notification.lib.entities.*;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Log4j2
@RestController
    @RequestMapping("${api.base.path}/notification")
public class NotificationAdminController extends LoBaseController {

    @Value("${api.base.path}")
    private String apiBasePath;

    @Autowired private NotificationAdminTransaction notificationAdminTransaction;

    @PostMapping(path = "/category", consumes = Constants.APPLICATION_JSON_CHARSET_UTF_8, produces = Constants.APPLICATION_JSON_CHARSET_UTF_8)
    public ResponseEntity<ApiResponse> addCategory(@RequestHeader Map<String, String> headers, @RequestBody String payload) {
        ApiResponse apiResponse = this.initializeResponse("POST : " + apiBasePath + "/category ->" + headers + payload);
        try {
            LcHeaderBO lcHeaderBO = this.getLcHeader(headers);
            JsonObject data = helper.fromJson(payload, JsonObject.class);
            String category = null;
            if(data.has("c_category")) {
                if (helper.isEmpty(data.get("c_category").getAsString()))
                throw new InvalidRequestException("Input","can't be empty");
            }
            this.validateInputPayload(data);

            category = data.get("c_category").getAsString();
            notificationAdminTransaction.addCategory(lcHeaderBO.getUserId(), category);
            this.addMessage(apiResponse, "Category Added Successfully");
        } catch (Exception e) {
            this.handleAppExceptions(e, apiResponse);
        }
        return this.getResponseEntity(apiResponse);
    }

    @GetMapping(path = "/list/category", produces = Constants.APPLICATION_JSON_CHARSET_UTF_8)
    public ResponseEntity<ApiResponse> listCategory(@RequestHeader Map<String, String> headers) {
        ApiResponse apiResponse = this.initializeResponse("GET : " + apiBasePath + "/list/category ->" + headers);
        try {
            LcHeaderBO lcHeaderBO = this.getLcHeader(headers);

            List<NotificationCategoryEntity> list = notificationAdminTransaction.listCategory();
            JsonArray categoryList = (JsonArray) helper.getGson().toJsonTree(list,
                    new TypeToken<List<NotificationCategoryEntity>>() {
                    }.getType());
            this.setDataJsonArrayPayload(apiResponse, categoryList);
        } catch (Exception e) {
            this.handleAppExceptions(e, apiResponse);
        }
        return this.getResponseEntity(apiResponse);
    }

    @DeleteMapping(path = "/category", consumes = Constants.APPLICATION_JSON_CHARSET_UTF_8, produces = Constants.APPLICATION_JSON_CHARSET_UTF_8)
    public ResponseEntity<ApiResponse> deleteCategory(@RequestHeader Map<String, String> headers, @RequestBody String payload) {
        ApiResponse apiResponse = this.initializeResponse("POST : " + apiBasePath + "/category ->" + headers + payload);
        try {
            LcHeaderBO lcHeaderBO = this.getLcHeader(headers);

            JsonObject data = helper.fromJson(payload, JsonObject.class);
            Long categoryId = data.get("n_category_id").getAsLong();
            this.validateInputPayload(data);

            notificationAdminTransaction.delete(lcHeaderBO.getUserId(), categoryId);
            this.addMessage(apiResponse, "Category Deleted Successfully");

        } catch (Exception e) {
            this.handleAppExceptions(e, apiResponse);
        }
        return this.getResponseEntity(apiResponse);
    }

    @PostMapping(path = "/product", consumes = Constants.APPLICATION_JSON_CHARSET_UTF_8, produces = Constants.APPLICATION_JSON_CHARSET_UTF_8)
    public ResponseEntity<ApiResponse> addProduct(@RequestHeader Map<String, String> headers, @RequestBody String payload) {
        ApiResponse apiResponse = this.initializeResponse("POST : " + apiBasePath + "/product ->" + headers + payload);
        try {
            LcHeaderBO lcHeaderBO = this.getLcHeader(headers);

            JsonObject data = helper.fromJson(payload, JsonObject.class);
            String productKey = null;
            String productName = null;
            if (data.has("c_product_key") && data.has("c_product_name")) {
                if (helper.isEmpty(data.get("c_product_key").getAsString()) || helper.isEmpty(data.get("c_product_name").getAsString()))
                    throw new InvalidRequestException("Input","can't be Empty");
            }
            this.validateInputPayload(data);

            productKey = data.get("c_product_key").getAsString();
            productName = data.get("c_product_name").getAsString();
            notificationAdminTransaction.addProduct(lcHeaderBO.getUserId(), productKey, productName);
            this.addMessage(apiResponse, "Product Added Successfully");

        } catch (Exception e) {
            this.handleAppExceptions(e, apiResponse);
        }
        return this.getResponseEntity(apiResponse);
    }

    @GetMapping(path = "/list/product", produces = Constants.APPLICATION_JSON_CHARSET_UTF_8)
    public ResponseEntity<ApiResponse> listProduct(@RequestHeader Map<String, String> headers) {
        ApiResponse apiResponse = this.initializeResponse("GET : " + apiBasePath + "/list/product ->" + headers);
        try {
            LcHeaderBO lcHeaderBO = this.getLcHeader(headers);


            List<NotificationProductEntity> list = notificationAdminTransaction.listProduct();
            JsonArray productList = (JsonArray) helper.getGson().toJsonTree(list,
                    new TypeToken<List<NotificationProductEntity>>() {
                    }.getType());
            this.setDataJsonArrayPayload(apiResponse, productList);
        } catch (Exception e) {
            this.handleAppExceptions(e, apiResponse);
        }

        return this.getResponseEntity(apiResponse);
    }

    @DeleteMapping(path = "/product", consumes = Constants.APPLICATION_JSON_CHARSET_UTF_8, produces = Constants.APPLICATION_JSON_CHARSET_UTF_8)
    public ResponseEntity<ApiResponse> deleteProduct(@RequestHeader Map<String, String> headers, @RequestBody String payload) {
        ApiResponse apiResponse = this.initializeResponse("POST : " + apiBasePath + "/product ->" + headers + payload);
        try {
            LcHeaderBO lcHeaderBO = this.getLcHeader(headers);

            JsonObject data = helper.fromJson(payload, JsonObject.class);

            String productKey =null;
            if(data.has("c_product_key")) {
                productKey = data.get("c_product_key").getAsString();
            }
            this.validateInputPayload(data);

            notificationAdminTransaction.deleteProduct(lcHeaderBO.getUserId(), productKey);
            this.addMessage(apiResponse, "Product Deleted Successfully");

        } catch (Exception e) {
            this.handleAppExceptions(e, apiResponse);
        }
        return this.getResponseEntity(apiResponse);
    }

    @PostMapping(path = "/topic", consumes = Constants.APPLICATION_JSON_CHARSET_UTF_8, produces = Constants.APPLICATION_JSON_CHARSET_UTF_8)
    public ResponseEntity<ApiResponse> addTopics(@RequestHeader Map<String, String> headers, @RequestBody String payload) {
        ApiResponse apiResponse = this.initializeResponse("POST : " + apiBasePath + "/topic ->" + headers + payload);
        try {
            LcHeaderBO lcHeaderBO = this.getLcHeader(headers);

            JsonObject json = helper.fromJson(payload, JsonObject.class);
            JsonArray topics = json.get("j_topics").getAsJsonArray();
            System.out.println(topics.size());
            String productKey = json.get("c_product_key").getAsString();

            if (json.has("j_topics") && json.has("c_product_key")) {
                if(helper.isEmpty(topics.get(0).getAsString())  || helper.isEmpty(productKey))
                    throw new InvalidRequestException("Input", "can't be empty");
                this.validateInputPayload(json);
            } else
                throw new RecordNotFoundException("Product key or topics missing");

            List<String> response = notificationAdminTransaction.addTopic(lcHeaderBO.getUserId(), topics, productKey);
            if (!response.isEmpty())
                this.addMessage(apiResponse, "Topic " + response + " Added Successfully");
            else
                this.addMessage(apiResponse, "Topic already added for this Product!");

        } catch (Exception e) {
            this.handleAppExceptions(e, apiResponse);
        }
        return this.getResponseEntity(apiResponse);
    }

    @PostMapping(path = "/list/topic", consumes = Constants.APPLICATION_JSON_CHARSET_UTF_8, produces = Constants.APPLICATION_JSON_CHARSET_UTF_8)
    public ResponseEntity<ApiResponse> listTopic(@RequestHeader Map<String, String> headers, @RequestBody String payload) {
        ApiResponse apiResponse = this.initializeResponse("POST : " + apiBasePath + "/list/topic ->" + headers + payload);
        try {
            LcHeaderBO lcHeaderBO = this.getLcHeader(headers);

            JsonObject jsonObject = helper.fromJson(payload, JsonObject.class);
            String productKey = null ;
            if (jsonObject.has("c_product_key")) {
                productKey = jsonObject.get("c_product_key").getAsString();
            } else
                throw new RecordNotFoundException("Product key or topics missing");

            this.validateInputPayload(payload);
            List<NotificationTopicEntity> list = notificationAdminTransaction.listTopic(productKey);
            JsonArray topicList = (JsonArray) helper.getGson().toJsonTree(list,
                    new TypeToken<List<NotificationTopicEntity>>() {
                    }.getType());
            this.setDataJsonArrayPayload(apiResponse, topicList);
        } catch (Exception e) {
            this.handleAppExceptions(e, apiResponse);
        }

        return this.getResponseEntity(apiResponse);
    }

    @DeleteMapping(path = "/topic", consumes = Constants.APPLICATION_JSON_CHARSET_UTF_8, produces = Constants.APPLICATION_JSON_CHARSET_UTF_8)
    public ResponseEntity<ApiResponse> deleteTopic(@RequestHeader Map<String, String> headers, @RequestBody String payload) {
        ApiResponse apiResponse = this.initializeResponse("POST : " + apiBasePath + "/topic ->" + headers + payload);
        try {
            LcHeaderBO lcHeaderBO = this.getLcHeader(headers);

            JsonObject jsonObject = helper.fromJson(payload, JsonObject.class);
            Long topicId = jsonObject.get("n_topic_id").getAsLong();
            if (jsonObject.has("n_topic_id")) {
                this.validateInputPayload(payload);
            } else
                throw new RecordNotFoundException("Product key or topics missing");

            notificationAdminTransaction.deleteTopic(lcHeaderBO.getUserId(), topicId);
            this.addMessage(apiResponse, "Topic Deleted Successfully");

        } catch (Exception e) {
            this.handleAppExceptions(e, apiResponse);
        }
        return this.getResponseEntity(apiResponse);
    }

    @PostMapping(path = "/service", consumes = Constants.APPLICATION_JSON_CHARSET_UTF_8, produces = Constants.APPLICATION_JSON_CHARSET_UTF_8)
    public ResponseEntity<ApiResponse> addService(@RequestHeader Map<String, String> headers, @RequestBody String payload) {
        ApiResponse apiResponse = this.initializeResponse("POST : " + apiBasePath + "/service ->" + headers + payload);
        try {
            LcHeaderBO lcHeaderBO = this.getLcHeader(headers);

            JsonObject data = helper.fromJson(payload, JsonObject.class);
            String endPoint = data.get("c_end_point").getAsString();
            String description = data.get("c_description").getAsString();
            String serviceName = data.get("c_service_name").getAsString();
            if (data.has("c_end_point") && data.has("c_description") && data.has("c_service_name")) {
                if(helper.isEmpty(endPoint) || helper.isEmpty(description) || helper.isEmpty(serviceName))
                    throw new InvalidRequestException("Input","can't be empty");
                this.validateInputPayload(data);
            } else
                throw new InvalidRequestException("c_service_name or c_end_point OR c_description", "Key not Found..!");

            notificationAdminTransaction.addService(lcHeaderBO.getUserId(), serviceName, endPoint, description);
            this.addMessage(apiResponse, "Service Added Successfully");

        } catch (Exception e) {
            this.handleAppExceptions(e, apiResponse);
        }
        return this.getResponseEntity(apiResponse);
    }

    @GetMapping(path = "/list/service", produces = Constants.APPLICATION_JSON_CHARSET_UTF_8)
    public ResponseEntity<ApiResponse> listService(@RequestHeader Map<String, String> headers) {
        ApiResponse apiResponse = this.initializeResponse("GET : " + apiBasePath + "/list/service ->"+ headers);
        try {
            LcHeaderBO lcHeaderBO = this.getLcHeader(headers);

            List<NotificationServiceEntity> list = notificationAdminTransaction.listService();
            JsonArray categoryList = (JsonArray) helper.getGson().toJsonTree(list,
                    new TypeToken<List<NotificationTopicEntity>>() {
                    }.getType());
            this.setDataJsonArrayPayload(apiResponse, categoryList);
        } catch (Exception e) {
            this.handleAppExceptions(e, apiResponse);
        }

        return this.getResponseEntity(apiResponse);
    }

    @DeleteMapping(path = "/service", consumes = Constants.APPLICATION_JSON_CHARSET_UTF_8, produces = Constants.APPLICATION_JSON_CHARSET_UTF_8)
    public ResponseEntity<ApiResponse> deleteService(@RequestHeader Map<String, String> headers, @RequestBody String payload) {
        ApiResponse apiResponse = this.initializeResponse("POST : " + apiBasePath + "/service ->" + headers + payload);
        try {
            LcHeaderBO lcHeaderBO = this.getLcHeader(headers);

            JsonObject data = helper.fromJson(payload, JsonObject.class);
            String serviceName = data.get("c_service_name").getAsString();
            if (data.has("c_service_name")) {
                this.validateInputPayload(data);
            } else
                throw new InvalidRequestException("c_service_name", "Key not Found..!");

            notificationAdminTransaction.deleteService(lcHeaderBO.getUserId(), serviceName);
            this.addMessage(apiResponse, "Service Deleted Successfully");

        } catch (Exception e) {
            this.handleAppExceptions(e, apiResponse);
        }
        return this.getResponseEntity(apiResponse);
    }

    @PostMapping(path = "/config", consumes = Constants.APPLICATION_JSON_CHARSET_UTF_8, produces = Constants.APPLICATION_JSON_CHARSET_UTF_8)
    public ResponseEntity<ApiResponse> addConfig(@RequestHeader Map<String, String> headers, @RequestBody String payload) throws InvalidRequestException {
        ApiResponse apiResponse = this.initializeResponse("POST : " + apiBasePath + "/config ->" + headers + payload);
        try {
            LcHeaderBO lcHeaderBO = this.getLcHeader(headers);

            JsonObject data = helper.fromJson(payload, JsonObject.class);
            String productKey = data.get("c_product_key").getAsString();
            String serviceName = data.get("c_service_name").getAsString();
            String customerId = data.get("c_customer_id").getAsString();
            String serviceOptions = data.get("c_service_options").getAsString();
            Long topicId = data.get("n_topic_id").getAsLong();
            Long deleteAfter = data.get("n_delete_after").getAsLong();
            if (data.has("c_service_name") && data.has("c_customer_id")
                    && data.has("n_topic_id") && data.has("c_product_key") && data.has("c_service_options")) {
                this.validateInputPayload(data);
            } else
                throw new InvalidRequestException("c_service_name", "Key not Found..!");

            notificationAdminTransaction.addConfig(lcHeaderBO.getUserId(), customerId, productKey, topicId, serviceName, serviceOptions, deleteAfter);
            this.addMessage(apiResponse, "Config Added Successfully");

        } catch (Exception e) {
            this.handleAppExceptions(e, apiResponse);
        }
        return this.getResponseEntity(apiResponse);
    }

    @PostMapping(path = "/updateDeviceToken", consumes = Constants.APPLICATION_JSON_CHARSET_UTF_8, produces = Constants.APPLICATION_JSON_CHARSET_UTF_8)
    public ResponseEntity<ApiResponse> updateConfig(@RequestHeader Map<String, String> headers, @RequestBody String payload) throws InvalidRequestException {
        ApiResponse apiResponse = this.initializeResponse("POST : " + apiBasePath + "/config ->" + headers + payload);
        try {
            LcHeaderBO lcHeaderBO = this.getLcHeader(headers);

            JsonObject data = helper.fromJson(payload, JsonObject.class);

            if (data.has("c_fcm_device_token") && data.has("c_product_key")
                    && data.has("c_service_name") && data.has("j_topics")) {
                this.validateInputPayload(data);
            } else
                throw new InvalidRequestException("c_product_key,c_service_name,c_fcm_device_token, j_topics", "Key not Found..!");

            String productKey = data.get("c_product_key").getAsString();
            JsonArray topics = data.get("j_topics").getAsJsonArray();
            String serviceName = data.get("c_service_name").getAsString();
            String deviceToken = data.get("c_fcm_device_token").getAsString();
            List<String> response = notificationAdminTransaction.updateConfig(lcHeaderBO.getUserId(), productKey, serviceName, topics, deviceToken);
            if (!response.isEmpty())
                this.addMessage(apiResponse, " Config added successfully for " + response + " topics ");
            else
                this.addMessage(apiResponse, "Topic not found. Config not Added!");

        } catch (Exception e) {
            this.handleAppExceptions(e, apiResponse);
        }
        return this.getResponseEntity(apiResponse);
    }

    @DeleteMapping(path = "/config", consumes = Constants.APPLICATION_JSON_CHARSET_UTF_8, produces = Constants.APPLICATION_JSON_CHARSET_UTF_8)
    public ResponseEntity<ApiResponse> deleteConfig(@RequestHeader Map<String, String> headers, @RequestBody String payload) throws InvalidRequestException {
        ApiResponse apiResponse = this.initializeResponse("POST : " + apiBasePath + "/config ->" + headers + payload);
        try {
            LcHeaderBO lcHeaderBO = this.getLcHeader(headers);

            JsonObject data = helper.fromJson(payload, JsonObject.class);
            String productKey = data.get("c_product_key").getAsString();
            String serviceName = data.get("c_service_name").getAsString();
            String customerId = data.get("c_customer_id").getAsString();
            Long topicId = data.get("n_topic_id").getAsLong();
            if (data.has("c_service_name") && data.has("c_customer_id")
                    && data.has("n_topic_id") && data.has("c_product_key")) {
                this.validateInputPayload(data);
            } else
                throw new InvalidRequestException("c_customer_id OR c_service_name OR c_product_key OR n_topic_id ", "Key not Found..!");

            notificationAdminTransaction.deleteConfig(lcHeaderBO.getUserId(), productKey, serviceName, customerId, topicId);
            this.addMessage(apiResponse, "Config deleted Successfully");

        } catch (Exception e) {
            this.handleAppExceptions(e, apiResponse);
        }
        return this.getResponseEntity(apiResponse);
    }

    @PostMapping(path = "/list/config", consumes = Constants.APPLICATION_JSON_CHARSET_UTF_8, produces = Constants.APPLICATION_JSON_CHARSET_UTF_8)
    public ResponseEntity<ApiResponse> ListConfig(@RequestHeader Map<String, String> headers, @RequestBody String payload) throws InvalidRequestException {
        ApiResponse apiResponse = this.initializeResponse("POST : " + apiBasePath + "/list/config ->" + headers + payload);
        try {
            JsonObject data = helper.fromJson(payload, JsonObject.class);
            String status = data.get("c_status").getAsString();
            String customerId = data.get("n_customer_id").getAsString();
            if (data.has("c_status") && data.has("n_customer_id")) {
                this.validateInputPayload(data);
            } else
                throw new InvalidRequestException("c_status OR n_customer_id ", "Key not Found..!");

            List<NotificationConfigEntity> list = notificationAdminTransaction.ListConfig(status, customerId);
            JsonArray configList = (JsonArray) helper.getGson().toJsonTree(list,
                    new TypeToken<List<NotificationTopicEntity>>() {
                    }.getType());
            this.setDataJsonArrayPayload(apiResponse, configList);
        } catch (Exception e) {
            this.handleAppExceptions(e, apiResponse);
        }
        return this.getResponseEntity(apiResponse);
    }

    @PostMapping(path = "/user", consumes = Constants.APPLICATION_JSON_CHARSET_UTF_8, produces = Constants.APPLICATION_JSON_CHARSET_UTF_8)
    public ResponseEntity<ApiResponse> addNotifications(@RequestHeader Map<String, String> headers, @RequestBody String payload) throws InvalidRequestException {
        ApiResponse apiResponse = this.initializeResponse("POST : " + apiBasePath + "/user ->" + headers + payload);
        try {
            LcHeaderBO lcHeaderBO = this.getLcHeader(headers);

            JsonObject data = helper.fromJson(payload, JsonObject.class);
            this.validateInputPayload(data);

            notificationAdminTransaction.addNotification(lcHeaderBO.getUserId(), data);

            this.addMessage(apiResponse, "Notification Added Successfully");
        } catch (Exception e) {
            this.handleAppExceptions(e, apiResponse);
        }
        return this.getResponseEntity(apiResponse);
    }

    @DeleteMapping(path = "/clearAll", consumes = Constants.APPLICATION_JSON_CHARSET_UTF_8, produces = Constants.APPLICATION_JSON_CHARSET_UTF_8)
    public ResponseEntity<ApiResponse> deleteNotification(@RequestHeader Map<String, String> headers, @RequestBody String payload) {
        ApiResponse apiResponse = this.initializeResponse("POST : " + apiBasePath + "/user ->" + headers + payload);
        try {
            LcHeaderBO lcHeaderBO = this.getLcHeader(headers);

            JsonObject data = helper.fromJson(payload, JsonObject.class);
            JsonArray notificationId = data.get("n_notification_id").getAsJsonArray();
            if (data.has("n_notification_id") && data.has("c_product_key")) {
                if (data.get("c_product_key").getAsString().equals(""))
                    throw new InputPayloadException("c_product_key", "can't be null");
                this.validateInputPayload(data);
            } else
                throw new InvalidRequestException("n_notification_id or c_product_key", "Key not Found..!");

            notificationAdminTransaction.deleteNotification(lcHeaderBO.getUserId(),notificationId,data.get("c_product_key").getAsString());
            this.addMessage(apiResponse, "Notification Deleted Successfully");
        } catch (Exception e) {
            this.handleAppExceptions(e, apiResponse);
        }
        return this.getResponseEntity(apiResponse);
    }

    @PostMapping(path = "/mark/read", consumes = Constants.APPLICATION_JSON_CHARSET_UTF_8, produces = Constants.APPLICATION_JSON_CHARSET_UTF_8)
    public ResponseEntity<ApiResponse> markRead(@RequestHeader Map<String, String> headers, @RequestBody String payload) throws InvalidRequestException {
        ApiResponse apiResponse = this.initializeResponse("POST : " + apiBasePath + "/mark/read ->" + payload);
        try {
            LcHeaderBO lcHeaderBO = this.getLcHeader(headers);
            JsonObject data = helper.fromJson(payload, JsonObject.class);
            JsonArray notificationId = data.get("n_notification_id").getAsJsonArray();
            if (data.has("n_notification_id") && data.has("c_product_key")) {
                if (data.get("c_product_key").getAsString().equals(""))
                    throw new InputPayloadException("c_product_key", "can't be null");
                this.validateInputPayload(data);
            } else
                throw new InvalidRequestException("n_notification_id or c_product_key", "Key not Found..!");

            notificationAdminTransaction.markNotificationRead(lcHeaderBO.getUserId(), notificationId, data.get("c_product_key").getAsString());
            this.addMessage(apiResponse, "Notifications Marked as Read");
        } catch (Exception e) {
            this.handleAppExceptions(e, apiResponse);
        }
        return this.getResponseEntity(apiResponse);
    }

    @PostMapping(path = "/mark/unread", consumes = Constants.APPLICATION_JSON_CHARSET_UTF_8, produces = Constants.APPLICATION_JSON_CHARSET_UTF_8)
    public ResponseEntity<ApiResponse> markUnread(@RequestHeader Map<String, String> headers, @RequestBody String payload) throws InvalidRequestException {
        ApiResponse apiResponse = this.initializeResponse("POST : " + apiBasePath + "/mark/unRead ->" + payload);
        try {
            LcHeaderBO lcHeaderBO = this.getLcHeader(headers);

            JsonObject data = helper.fromJson(payload, JsonObject.class);
            JsonArray notificationId = data.get("n_notification_id").getAsJsonArray();
            if (data.has("n_notification_id") && data.has("c_product_key")) {
                if (data.get("c_product_key").getAsString().equals(""))
                    throw new InputPayloadException("c_product_key", "can't be null");
                this.validateInputPayload(data);
            } else
                throw new InvalidRequestException("n_notification_id or c_product_key", "Key not Found..!");

            notificationAdminTransaction.markAsUnread(lcHeaderBO.getUserId(), notificationId, data.get("c_product_key").getAsString());
            this.addMessage(apiResponse, "Notifications Marked as Unread");
        } catch (Exception e) {
            this.handleAppExceptions(e, apiResponse);
        }
        return this.getResponseEntity(apiResponse);
    }

    @PostMapping(path = "/unread", consumes = Constants.APPLICATION_JSON_CHARSET_UTF_8, produces = Constants.APPLICATION_JSON_CHARSET_UTF_8)
    public ResponseEntity<ApiResponse> listUnreadNotification(@RequestHeader Map<String, String> headers, @RequestBody String payload) throws InvalidRequestException {
        ApiResponse apiResponse = this.initializeResponse("POST : " + apiBasePath + "/unRead ->" + headers + payload);
        try {
            LcHeaderBO lcHeaderBO = this.getLcHeader(headers);

            PageBO pageBO = helper.fromJson(payload, PageBO.class);
            this.validateInputPayload(pageBO);

            List<NotificationUserEntity> list = notificationAdminTransaction.listUnreadNotification(helper.getString(lcHeaderBO.getUserId()),pageBO);
            JsonArray unreadNotificationList = (JsonArray) helper.getGson().toJsonTree(list,
                    new TypeToken<List<NotificationUserEntity>>() {
                    }.getType());
            this.setDataJsonArrayPayload(apiResponse, unreadNotificationList);
        } catch (Exception e) {
            this.handleAppExceptions(e, apiResponse);
        }
        return this.getResponseEntity(apiResponse);
    }
    @PostMapping(path = "/unread/count",consumes = Constants.APPLICATION_JSON_CHARSET_UTF_8, produces = Constants.APPLICATION_JSON_CHARSET_UTF_8)
    public ResponseEntity<ApiResponse> unreadNotificationCount(@RequestHeader Map<String, String> headers, @RequestBody String payload) {
        ApiResponse apiResponse = this.initializeResponse("GET : " + apiBasePath + "/unRead/count ->" + headers );
        try {
            LcHeaderBO lcHeaderBO = this.getLcHeader(headers);
            JsonObject jsonObject = helper.fromJson(payload,JsonObject.class);
            if(jsonObject.get("c_product_key").getAsString().equals("") || helper.isEmpty(jsonObject.get("c_product_key").getAsString()))
                throw new InputPayloadException("c_product_key","can't be null");

            this.validateInputPayload(jsonObject);
            long count = notificationAdminTransaction.unreadNotificationCount(jsonObject.get("c_product_key").getAsString(),helper.getString(lcHeaderBO.getUserId()));
            JsonObject data = new JsonObject();
            data.addProperty("n_total", count);
            this.setDataJsonObjectPayload(apiResponse, data);
        } catch (Exception e) {
            this.handleAppExceptions(e, apiResponse);
        }
        return this.getResponseEntity(apiResponse);
    }

    @PostMapping(path = "/all", consumes = Constants.APPLICATION_JSON_CHARSET_UTF_8, produces = Constants.APPLICATION_JSON_CHARSET_UTF_8)
    public ResponseEntity<ApiResponse> listAllNotification(@RequestHeader Map<String, String> headers, @RequestBody String payload) throws InvalidRequestException {
        ApiResponse apiResponse = this.initializeResponse("POST : " + apiBasePath + "/all ->" + headers + payload);
        try {
            LcHeaderBO lcHeaderBO = this.getLcHeader(headers);
            JsonObject jsonObject = helper.fromJson(payload,JsonObject.class);
            PageBO pageBO = helper.fromJson(payload, PageBO.class);
            String productKey = null;
            String topic = "";
            if (jsonObject.has("c_product_key")) {
                if (helper.isEmpty(jsonObject.get("c_product_key").getAsString()) || jsonObject.get("c_product_key").getAsString().equals(""))
                    throw new InputPayloadException("c_product_key", "can't be null");
                else {
                    productKey = jsonObject.get("c_product_key").getAsString();
                    if (productKey.equals("TS") && jsonObject.has("c_notification_type")) {
                        {
                            topic = jsonObject.get("c_notification_type").getAsString();
                            if (topic.equals("") || !topic.equals(com.c2.lc.ms.notification.lib.utils.Constants.TS_NOTIFICATION_ALL) && !topic.equals(com.c2.lc.ms.notification.lib.utils.Constants.TS_NOTIFICATION_ORDER)
                                    && !topic.equals(com.c2.lc.ms.notification.lib.utils.Constants.TS_NOTIFICATION_STOCK))
                                throw new InputPayloadException("All, Order and Stock", "are the valid types");
                        }
                    }
                }
            } else
                throw new InputPayloadException("c_product_key", "Key missing");

            JsonArray list = notificationAdminTransaction.listAllNotification(productKey,topic,lcHeaderBO.getUserId(),pageBO);
            JsonObject data = new JsonObject();
            data.add("data", list);
            data.addProperty("n_next_page", pageBO.getPage() + 1);

            this.setDataJsonArrayPayload(apiResponse, list);
        } catch (Exception e) {
            this.handleAppExceptions(e, apiResponse);
        }
        return this.getResponseEntity(apiResponse);
    }

    @PostMapping(path = "/count/all", consumes = Constants.APPLICATION_JSON_CHARSET_UTF_8, produces = Constants.APPLICATION_JSON_CHARSET_UTF_8)
    public ResponseEntity<ApiResponse> listAllNotificationCount(@RequestHeader Map<String, String> headers, @RequestBody String payload) throws InvalidRequestException {
        ApiResponse apiResponse = this.initializeResponse("POST : " + apiBasePath + "/all ->" + headers + payload);
        try {
            LcHeaderBO lcHeaderBO = this.getLcHeader(headers);
            JsonObject jsonObject = helper.fromJson(payload,JsonObject.class);
            this.validateInputPayload(jsonObject);
            String productKey = null;
            String topic = "";
            if (jsonObject.has("c_product_key")) {
                if (helper.isEmpty(jsonObject.get("c_product_key").getAsString()) || jsonObject.get("c_product_key").getAsString().equals(""))
                    throw new InputPayloadException("c_product_key", "can't be null");
                else {
                    productKey = jsonObject.get("c_product_key").getAsString();
                    if (productKey.equals("TS") && jsonObject.has("c_notification_type")) {
                        {
                            topic = jsonObject.get("c_notification_type").getAsString();
                            if (topic.equals("") || !topic.equals(com.c2.lc.ms.notification.lib.utils.Constants.TS_NOTIFICATION_ALL) && !topic.equals(com.c2.lc.ms.notification.lib.utils.Constants.TS_NOTIFICATION_ORDER)
                                    && !topic.equals(com.c2.lc.ms.notification.lib.utils.Constants.TS_NOTIFICATION_STOCK))
                                throw new InputPayloadException("All, Order and Stock", "are the valid types");
                        }
                    }
                }
            } else
                throw new InputPayloadException("c_product_key", "Key missing");

            long count = notificationAdminTransaction.listAllNotificationCount(productKey,topic,lcHeaderBO.getUserId());
            JsonObject data = new JsonObject();
            data.addProperty("n_total", count);

            this.setDataJsonObjectPayload(apiResponse, data);
        } catch (Exception e) {
            this.handleAppExceptions(e, apiResponse);
        }
        return this.getResponseEntity(apiResponse);
    }

    @DeleteMapping(path = "/expired", produces = Constants.APPLICATION_JSON_CHARSET_UTF_8)
    public ResponseEntity<ApiResponse> deleteExpiredNotification(@RequestHeader Map<String, String> headers) throws InvalidRequestException {
        ApiResponse apiResponse = this.initializeResponse("POST : " + apiBasePath + "/expired ->" + headers);
        try {
            LcHeaderBO lcHeaderBO = this.getLcHeader(headers);

            notificationAdminTransaction.deleteExpiredNotification(lcHeaderBO.getUserId());

            this.addMessage(apiResponse, "Expired Notification Deleted Successfully");
        } catch (Exception e) {
            this.handleAppExceptions(e, apiResponse);
        }
        return this.getResponseEntity(apiResponse);
    }

    @PostMapping(path = "/updateNotification", consumes = Constants.APPLICATION_JSON_CHARSET_UTF_8, produces = Constants.APPLICATION_JSON_CHARSET_UTF_8)
    public ResponseEntity<ApiResponse> updateDeliveredNotification(@RequestHeader Map<String, String> headers, @RequestBody String payload) throws InvalidRequestException {
        ApiResponse apiResponse = this.initializeResponse("POST : " + apiBasePath + "/updateNotification ->" + payload);
        try {
            JsonObject data = helper.fromJson(payload, JsonObject.class);
            JsonArray notificationId = data.get("n_notification_id").getAsJsonArray();
            if (data.has("n_notification_id")) {
                this.validateInputPayload(data);
            } else
                throw new InvalidRequestException("n_notification_id", "Key not Found..!");

            notificationAdminTransaction.updateDeliveredNotification(notificationId);
            this.addMessage(apiResponse, "Notifications delivered time updated");
        } catch (Exception e) {
            this.handleAppExceptions(e, apiResponse);
        }
        return this.getResponseEntity(apiResponse);
    }

}
