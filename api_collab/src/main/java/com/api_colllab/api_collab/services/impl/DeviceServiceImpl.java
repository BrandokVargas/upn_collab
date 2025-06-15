package com.api_colllab.api_collab.services.impl;

import com.api_colllab.api_collab.controller.dto.NotificationDTO;
import com.api_colllab.api_collab.controller.dto.RequestDeviceDTO;
import com.api_colllab.api_collab.controller.dto.ResponseDevice;
import com.api_colllab.api_collab.controller.dto.ResponseNotification;
import com.api_colllab.api_collab.persistence.entity.DeviceEntityFormUser;
import com.api_colllab.api_collab.persistence.entity.UserEntity;
import com.api_colllab.api_collab.persistence.repository.DeviceRepository;
import com.api_colllab.api_collab.persistence.repository.UserRepository;
import com.api_colllab.api_collab.services.IDeviceService;
import com.api_colllab.api_collab.services.tokenMessagin.FirebaseAuthHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;


import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import java.util.List;
import java.util.Optional;


@Service
public class DeviceServiceImpl implements IDeviceService {

    private static final String URL = "https://fcm.googleapis.com/v1/projects/collabnotification-b6498/messages:send";

    @Autowired
    private DeviceRepository deviceRepository;

    @Autowired
    private UserRepository userRepository;


    public static String getTokenAcces(){
        try {
            return FirebaseAuthHelper.getAccessToken();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public ResponseDevice registerDevice(RequestDeviceDTO deviceDTO) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String name = authentication.getName();
        UserEntity user = userRepository.findUser(name);

        DeviceEntityFormUser device = deviceRepository.existDeviceId(deviceDTO.token_device());
        if (device != null) {
            return ResponseDevice.builder()
                    .id(device.getId_device())
                    .token_device(device.getToken_device())
                    .rpta(1)
                    .body(new RequestDeviceDTO(device.getToken_device(),user.getId_user()))
                    .build();
        } else {
            DeviceEntityFormUser newDevice = DeviceEntityFormUser.builder()
                    .token_device(deviceDTO.token_device())
                    .user(user)
                    .build();
            DeviceEntityFormUser savedDevice = deviceRepository.save(newDevice);
            return ResponseDevice.builder()
                    .id(savedDevice.getId_device())
                    .token_device(savedDevice.getToken_device())
                    .rpta(1)
                    .body(new RequestDeviceDTO(savedDevice.getToken_device(),user.getId_user()))
                    .build();
        }
    }

    @Override
    public ResponseNotification sendNotification(NotificationDTO notification,Long id_user) throws MalformedURLException {
        Optional<DeviceEntityFormUser> findIdDevice = deviceRepository.findByUserId(id_user);

        if (!findIdDevice.isPresent()) {
            return ResponseNotification.builder()
                    .rpta(0)
                    .message("No existe este dispositivo")
                    .notification(notification)
                    .build();
        }

        NotificationDTO addNotification = NotificationDTO.builder()
                .id_device(findIdDevice.get().getToken_device())
                .title(notification.title())
                .message(notification.message())
                .build();

        HttpURLConnection connection = null;
        OutputStream os = null;

        try {
            URL url = new URL(URL);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Authorization", "Bearer " + getTokenAcces());
            connection.setRequestProperty("Content-Type", "application/json; UTF-8");
            connection.setDoOutput(true);

            String body = buildNotificationBody(addNotification);

            os = connection.getOutputStream();
            os.write(body.getBytes("UTF-8"));
            os.flush();

            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                return ResponseNotification.builder()
                        .rpta(1)
                        .message("Notificacion enviada correctamente")
                        .notification(addNotification)
                        .build();
            } else {
                return ResponseNotification.builder()
                        .rpta(0)
                        .message("Error al enviar la notificacion: " + responseCode)
                        .notification(addNotification)
                        .build();
            }
        } catch (MalformedURLException e) {
            return ResponseNotification.builder()
                    .rpta(0)
                    .message("Malformed URL Exception: " + e.getMessage())
                    .notification(addNotification)
                    .build();
        } catch (IOException e) {
            return ResponseNotification.builder()
                    .rpta(0)
                    .message("IO Exception: " + e.getMessage())
                    .notification(addNotification)
                    .build();
        } catch (Exception e) {
            return ResponseNotification.builder()
                    .rpta(0)
                    .message("Exception: " + e.getMessage())
                    .notification(addNotification)
                    .build();
        } finally {
            if (os != null) {
                try {
                    os.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (connection != null) {
                connection.disconnect();
            }
        }
    }

    @Override
    public ResponseNotification sendAllNotification(NotificationDTO notification) throws MalformedURLException {
        List<DeviceEntityFormUser> findAllDevice = (List<DeviceEntityFormUser>) deviceRepository.findAll();
        if (findAllDevice.isEmpty()) {
            return ResponseNotification.builder()
                    .rpta(0)
                    .message("No existen dispositivos registrados en la base de datos")
                    .notification(notification)
                    .build();
        }

        HttpURLConnection connection = null;
        OutputStream os = null;

        try {
            for (DeviceEntityFormUser device : findAllDevice) {
                NotificationDTO addNotification = NotificationDTO.builder()
                        .id_device(device.getToken_device())
                        .title(notification.title())
                        .message(notification.message())
                        .build();

                URL url = new URL(URL);
                connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("POST");
                connection.setRequestProperty("Authorization", "Bearer " + getTokenAcces());
                connection.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
                connection.setDoOutput(true);

                String body = buildNotificationBody(addNotification);
                os = connection.getOutputStream();
                os.write(body.getBytes("UTF-8"));
                os.flush();

                int responseCode = connection.getResponseCode();
                System.out.println("RESPONSE: " + responseCode);
                if (responseCode != HttpURLConnection.HTTP_OK) {

                    return ResponseNotification.builder()
                            .rpta(0)
                            .message("Error al enviar la notificacion: " + responseCode)
                            .notification(addNotification)
                            .build();
                }
            }

            return ResponseNotification.builder()
                    .rpta(1)
                    .message("Notificaciones enviadas correctamente a todos los dispositivos")
                    .notification(notification)
                    .build();

        } catch (IOException e) {
            e.printStackTrace();
            return ResponseNotification.builder()
                    .rpta(0)
                    .message("Error de IO al enviar la notificacion: " + e.getMessage())
                    .notification(notification)
                    .build();
        } finally {
            if (os != null) {
                try {
                    os.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (connection != null) {
                connection.disconnect();
            }
        }

    }

    @Override
    public String buildNotificationBody(NotificationDTO notification) {
        return "{"
                + "\"message\": {"
                + "\"token\": \"" + notification.id_device() + "\","
                + "\"data\": {"
                + "\"title\": \"" + notification.title() + "\","
                + "\"message\": \"" + notification.message() + "\","
                + "\"fragment\": \"ForumFragment\""
                + "}"
                + "}"
                + "}";
    }


}
