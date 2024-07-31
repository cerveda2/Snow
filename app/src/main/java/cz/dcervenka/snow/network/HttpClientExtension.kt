package cz.dcervenka.snow.network

import cz.dcervenka.snow.util.DataError
import cz.dcervenka.snow.util.Result
import retrofit2.HttpException
import retrofit2.Response
import java.io.IOException
import kotlin.coroutines.cancellation.CancellationException

inline fun <reified T> safeCall(execute: () -> Response<T>): Result<T, DataError.Network> {
    val response: Response<T> = try {
        execute()
    } catch (e: IOException) {
        e.printStackTrace()
        return Result.Error(DataError.Network.NO_INTERNET)
    } catch (e: HttpException) {
        e.printStackTrace()
        return Result.Error(DataError.Network.SERIALIZATION)
    } catch (e: Exception) {
        if (e is CancellationException) throw e
        e.printStackTrace()
        return Result.Error(DataError.Network.UNKNOWN)
    }

    return responseToResult(response)
}

inline fun <reified T> responseToResult(response: Response<T>): Result<T, DataError.Network> {
    return when (response.code()) {
        in 200..299 -> response.body()?.let {
            Result.Success(it)
        } ?: Result.Error(DataError.Network.SERIALIZATION)
        408 -> Result.Error(DataError.Network.REQUEST_TIMEOUT)
        409 -> Result.Error(DataError.Network.CONFLICT)
        429 -> Result.Error(DataError.Network.TOO_MANY_REQUESTS)
        in 500..599 -> Result.Error(DataError.Network.SERVER_ERROR)
        else -> Result.Error(DataError.Network.UNKNOWN)
    }
}