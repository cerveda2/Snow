package cz.dcervenka.snow.ui.overview

sealed interface OverviewAction {
    data object OnDetailClick : OverviewAction
    data class OnSearchTextChanged(val text: String) : OverviewAction
    data class OnListExpand(val expand: Boolean) : OverviewAction
    data object OnFavoritePlace : OverviewAction
}