package cz.dcervenka.snow.ui.util

import cz.dcervenka.snow.R
import cz.dcervenka.snow.util.DataError

fun DataError.asUiText(): UiText {
    return when (this) {
        DataError.Network.REQUEST_TIMEOUT -> UiText.StringResource(R.string.error_request_timeout)
        DataError.Network.TOO_MANY_REQUESTS -> UiText.StringResource(R.string.error_too_many_requests)
        DataError.Network.NO_INTERNET -> UiText.StringResource(R.string.error_no_internet)
        DataError.Network.SERVER_ERROR -> UiText.StringResource(R.string.error_server_error)
        DataError.Network.SERIALIZATION -> UiText.StringResource(R.string.error_serialization)
        else -> UiText.StringResource(R.string.error_unknown)
    }
}