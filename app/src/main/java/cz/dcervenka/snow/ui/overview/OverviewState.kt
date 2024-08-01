package cz.dcervenka.snow.ui.overview

import androidx.compose.ui.text.input.TextFieldValue
import cz.dcervenka.snow.model.ResponseData

data class OverviewState(
    val search: TextFieldValue = TextFieldValue(),
    val showOnlyFavorites: Boolean = false,
    val data: ResponseData = ResponseData()
)
