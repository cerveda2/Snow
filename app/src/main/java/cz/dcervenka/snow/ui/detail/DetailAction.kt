package cz.dcervenka.snow.ui.detail

import cz.dcervenka.snow.ui.overview.OverviewAction

sealed interface DetailAction {
    data class OnFavoritePlace(val favorite: Boolean) : DetailAction
    data object OnVisitPlace : DetailAction
}