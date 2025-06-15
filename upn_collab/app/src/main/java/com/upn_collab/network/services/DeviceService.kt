package com.upn_collab.network.services

import com.upn_collab.network.dto.auth.DeviceDTO
import com.upn_collab.network.dto.auth.NotificationDTO
import com.upn_collab.network.dto.response.ResponseDevice
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Path

interface DeviceService {

    @POST("api/register-device")
    suspend fun registerDevice(@Body deviceDTO: DeviceDTO) : Response<ResponseDevice>

    @POST("api/sendNotification/{id_user}")
    suspend fun sendNotification(@Path("id_user") id_user: Long,@Body notificationDTO: NotificationDTO):Response<NotificationDTO>

    @POST("api/sendAllNotification")
    suspend fun sendNotificationAll(@Body notificationDTO: NotificationDTO):Response<NotificationDTO>

}