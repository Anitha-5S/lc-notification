package com.c2.lc.ms.notification.lib.services.interfaces;

import com.c2.lc.lib.services.interfaces.BaseDBService;

import java.util.List;

public interface NotificationBaseService extends BaseDBService {

    void add(Object entity);

    void delete(Object entity);

    List<Object> list ();
}
