package cz.dcervenka.snow.ui.overview

sealed interface OverviewAction {
    data object OnDetailClick : OverviewAction
    data class OnListExpand(val expand: Boolean) : OverviewAction
    data class OnFavoritePlace(val favorite: Boolean) : OverviewAction
}