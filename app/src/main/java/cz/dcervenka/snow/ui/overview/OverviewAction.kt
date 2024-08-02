package cz.dcervenka.snow.ui.overview

sealed interface OverviewAction {
    data class OnDetailClick(val resortId: String) : OverviewAction
    data class OnSearchTextChanged(val text: String) : OverviewAction
    data object OnFavoriteToggled : OverviewAction
    data class OnFavoriteSet(val resortId: String) : OverviewAction
}