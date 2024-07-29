package cz.dcervenka.snow.network

import cz.dcervenka.snow.model.ResponseData
import cz.dcervenka.snow.util.DataError
import cz.dcervenka.snow.util.Result
import retrofit2.http.GET
import retrofit2.http.Query

interface SnowService {

    @GET("allinone")
    suspend fun loadPlaces(@Query("lang") language: String): Result<ResponseData, DataError.Network>
}