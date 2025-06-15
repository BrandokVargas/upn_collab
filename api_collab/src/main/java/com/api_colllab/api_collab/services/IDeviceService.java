package com.api_colllab.api_collab.services;

import com.api_colllab.api_collab.controller.dto.NotificationDTO;
import com.api_colllab.api_collab.controller.dto.RequestDeviceDTO;
import com.api_colllab.api_collab.controller.dto.ResponseDevice;
import com.api_colllab.api_collab.controller.dto.ResponseNotification;
import com.api_colllab.api_collab.persistence.entity.DeviceEntityFormUser;
import org.springframework.http.ResponseEntity;

import java.net.MalformedURLException;
import java.util.Optional;

public interface IDeviceService {

    ResponseDevice registerDevice(RequestDeviceDTO deviceDTO);


    ResponseNotification sendNotification(NotificationDTO notification,Long id_user) throws MalformedURLException;
    ResponseNotification sendAllNotification(NotificationDTO notification) throws MalformedURLException;




    String buildNotificationBody(NotificationDTO notificationDTO);

}
