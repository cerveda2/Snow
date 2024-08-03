package cz.dcervenka.snow.network

import com.squareup.moshi.JsonDataException
import cz.dcervenka.snow.util.DataError
import cz.dcervenka.snow.util.Result
import retrofit2.Response
import timber.log.Timber
import java.io.IOException
import java.net.SocketTimeoutException
import kotlin.coroutines.cancellation.CancellationException

inline fun <reified T> safeCall(execute: () -> Response<T>): Result<T, DataError.Network> {
    val response: Response<T> = try {
        execute()
    } catch (e: IOException) {
        Timber.e(e)
        return Result.Error(DataError.Network.NO_INTERNET)
    } catch (e: SocketTimeoutException) {
        Timber.e(e)
        return Result.Error(DataError.Network.REQUEST_TIMEOUT)
    } catch (e: JsonDataException) {
        Timber.e(e)
        return Result.Error(DataError.Network.SERIALIZATION)
    } catch (e: Exception) {
        if (e is CancellationException) throw e
        Timber.e(e)
        return Result.Error(DataError.Network.UNKNOWN)
    }

    return responseToResult(response)
}

inline fun <reified T> responseToResult(response: Response<T>): Result<T, DataError.Network> {
    return when (response.code()) {
        in 200..299 -> response.body()?.let {
            Result.Success(it)
        } ?: Result.Error(DataError.Network.SERIALIZATION)
        404 -> Result.Error(DataError.Network.NOT_FOUND)
        408 -> Result.Error(DataError.Network.REQUEST_TIMEOUT)
        429 -> Result.Error(DataError.Network.TOO_MANY_REQUESTS)
        in 500..599 -> Result.Error(DataError.Network.SERVER_ERROR)
        else -> Result.Error(DataError.Network.UNKNOWN)
    }
}