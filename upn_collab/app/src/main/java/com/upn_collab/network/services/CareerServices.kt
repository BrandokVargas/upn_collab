package com.upn_collab.network.services

import com.upn_collab.network.dto.response.CareersResponse
import retrofit2.Response
import retrofit2.http.GET

interface CareerServices {
    @GET("careers")
    suspend fun getCareers(): Response<List<CareersResponse>>
}