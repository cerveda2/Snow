package cz.dcervenka.snow.ui.overview

import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import cz.dcervenka.snow.ui.OverviewViewModel
import cz.dcervenka.snow.ui.components.ExpandableAreaList
import cz.dcervenka.snow.ui.components.SearchTextField
import cz.dcervenka.snow.ui.theme.SnowTheme

@Composable
fun OverviewScreenRoot(
    onDetailClick: () -> Unit,
    viewModel: OverviewViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    LaunchedEffect(Unit) {
        viewModel.errorEvent.collect { error ->
            Toast.makeText(
                context,
                error.asString(context),
                Toast.LENGTH_LONG
            ).show()
        }
    }

    OverviewScreen(
        state = viewModel.state,
        onAction = { action ->
            when (action) {
                OverviewAction.OnFavoriteToggled -> {
                    viewModel.toggleFavorite()
                }
                is OverviewAction.OnDetailClick -> {
                    viewModel.setDetailResort(action.resortId)
                    onDetailClick()
                }
                is OverviewAction.OnSearchTextChanged -> viewModel.search(action.text)
                is OverviewAction.OnFavoriteSet -> {
                    viewModel.setFavorite(
                        resortId = action.resortId,
                        fromDetail = false
                    )
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
                onTextChange = { newTextValue ->
                    searchState = newTextValue
                    onAction(OverviewAction.OnSearchTextChanged(newTextValue.text))
                }
            )
            ExpandableAreaList(
                responseData = state.data,
                showOnlyFavorites = state.showOnlyFavorites,
                searchInitiated = searchState.text.isNotEmpty() && searchState.text.length > 1,
                onDetailClick = { resortId ->
                    onAction(OverviewAction.OnDetailClick(resortId))
                },
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