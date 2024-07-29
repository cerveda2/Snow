package cz.dcervenka.snow.ui.detail

sealed interface DetailAction {
    data class OnFavoritePlace(val favorite: Boolean) : DetailAction
    data object OnVisitPlace : DetailAction
    data object OnBackClick : DetailAction
}