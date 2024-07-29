package cz.dcervenka.snow.network

import cz.dcervenka.snow.model.ResponseData
import retrofit2.Response
import retrofit2.http.GET

interface SnowService {

    @GET("allinone")
    suspend fun loadPlaces(): Response<ResponseData>
}