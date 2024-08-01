package cz.dcervenka.snow.ui.overview

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import cz.dcervenka.snow.ui.components.ExpandableAreaList
import cz.dcervenka.snow.ui.components.SearchTextField
import cz.dcervenka.snow.ui.theme.SnowTheme

@Composable
fun OverviewScreenRoot(
    onDetailClick: () -> Unit,
    viewModel: OverviewViewModel = hiltViewModel()
) {
    OverviewScreen(
        state = viewModel.state,
        onAction = { action ->
            when (action) {
                OverviewAction.OnDetailClick -> onDetailClick()
                is OverviewAction.OnFavoriteToggled -> {
                    viewModel.toggleFavorite()
                }
                is OverviewAction.OnSearchTextChanged -> Unit
                is OverviewAction.OnFavoriteSet -> {
                    viewModel.setFavorite(action.resortId)
                }
            }
        }
    )
}

@Composable
fun OverviewScreen(
    state: OverviewState,
    onAction: (OverviewAction) -> Unit,
) {
    var searchState by remember { mutableStateOf(state.search) }

    Scaffold { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            SearchTextField(
                textState = searchState,
                favoriteToggled = state.showOnlyFavorites,
                onFavoriteToggled = { onAction(OverviewAction.OnFavoriteToggled) },
                onTextChange = {
                    searchState = it
                    onAction(OverviewAction.OnSearchTextChanged(it.text))
                }
            )
            ExpandableAreaList(
                responseData = state.data,
                showOnlyFavorites = state.showOnlyFavorites,
                onDetailClick = {},
                onSetFavorite = { resortId -> onAction(OverviewAction.OnFavoriteSet(resortId)) },
            )
        }
    }

}

@Preview
@Composable
private fun OverviewScreenPreview() {
    SnowTheme {
        OverviewScreen(
            state = OverviewState(),
            onAction = {}
        )
    }
}