package com.upn_collab.network.dto.response

import com.upn_collab.network.dto.auth.DeviceDTO

data class ResponseDevice(val id: Long, val token_device: String, val rpta: Int, val body: DeviceDTO?)

