package com.api_colllab.api_collab.controller;


import com.api_colllab.api_collab.controller.dto.NotificationDTO;
import com.api_colllab.api_collab.controller.dto.RequestDeviceDTO;
import com.api_colllab.api_collab.controller.dto.ResponseDevice;
import com.api_colllab.api_collab.controller.dto.ResponseNotification;
import com.api_colllab.api_collab.services.IDeviceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.net.MalformedURLException;
import java.util.Optional;


@RestController
@RequestMapping("/api")
public class NotificationController {


    @Autowired
    private IDeviceService iDeviceService;

    @PostMapping("/register-device")
    public ResponseDevice registerDevice(@RequestBody RequestDeviceDTO requestDeviceDTO){
        return iDeviceService.registerDevice(requestDeviceDTO);
    }

    @PostMapping("/sendNotification/{id_user}")
    public ResponseNotification sendNotificationOnlyUser(@PathVariable("id_user") Long id_user,
                                                         @RequestBody NotificationDTO notificationDTO) throws MalformedURLException {
        return iDeviceService.sendNotification(notificationDTO,id_user);
    }

    @PostMapping("/sendAllNotification")
    public ResponseNotification sendNotificationAllUsers(@RequestBody NotificationDTO notificationDTO) throws MalformedURLException {
        return iDeviceService.sendAllNotification(notificationDTO);
    }


}
