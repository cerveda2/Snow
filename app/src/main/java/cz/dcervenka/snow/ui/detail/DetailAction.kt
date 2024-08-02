package cz.dcervenka.snow.ui.detail

sealed interface DetailAction {
    data class OnFavoriteSet(val resortId: String) : DetailAction
    data object OnVisitPlace : DetailAction
    data object OnBackClick : DetailAction
}