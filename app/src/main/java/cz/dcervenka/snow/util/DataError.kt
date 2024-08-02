package cz.dcervenka.snow.util

sealed interface DataError: Error {

    enum class Network: DataError {
        REQUEST_TIMEOUT,
        TOO_MANY_REQUESTS,
        NO_INTERNET,
        SERVER_ERROR,
        SERIALIZATION,
        NOT_FOUND,
        UNKNOWN
    }

    enum class Local: DataError {
        NO_FAVORITES,
        SEARCH_EMPTY,
    }
}