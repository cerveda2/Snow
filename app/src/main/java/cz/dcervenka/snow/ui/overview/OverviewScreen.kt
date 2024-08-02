package cz.dcervenka.snow.ui.overview

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import cz.dcervenka.snow.R
import cz.dcervenka.snow.ui.OverviewViewModel
import cz.dcervenka.snow.ui.components.ExpandableAreaList
import cz.dcervenka.snow.ui.components.SearchTextField
import cz.dcervenka.snow.ui.theme.SnowTheme
import cz.dcervenka.snow.ui.util.asUiText
import cz.dcervenka.snow.util.DataError

@Composable
fun OverviewScreenRoot(
    onDetailClick: () -> Unit,
    viewModel: OverviewViewModel = hiltViewModel()
) {
    val error by viewModel.errorEvent.collectAsStateWithLifecycle(null)
    val isLoading by viewModel.isLoading.collectAsStateWithLifecycle()

    OverviewScreen(
        state = viewModel.state,
        error = error,
        loading = isLoading,
        onAction = { action ->
            when (action) {
                OverviewAction.OnFavoriteToggled -> {
                    viewModel.toggleFavorite()
                }

                OverviewAction.OnRetry -> {
                    viewModel.loadPlaces()
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
    error: DataError?,
    loading: Boolean,
    onAction: (OverviewAction) -> Unit,
) {
    var searchState by remember { mutableStateOf(state.search) }

    Scaffold { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            if (loading) {
                CircularProgressIndicator(
                    modifier = Modifier
                        .padding(top = 60.dp)
                        .align(Alignment.CenterHorizontally)
                )
            } else {
                when (error) {
                    DataError.Network.REQUEST_TIMEOUT,
                    DataError.Network.TOO_MANY_REQUESTS,
                    DataError.Network.NO_INTERNET,
                    DataError.Network.SERVER_ERROR,
                    DataError.Network.NOT_FOUND,
                    DataError.Network.SERIALIZATION,
                    DataError.Network.UNKNOWN -> {
                        ErrorState(
                            error = error,
                            canRetry = true,
                            retry = { onAction(OverviewAction.OnRetry) }
                        )
                    }

                    else -> {
                        SearchTextField(
                            textState = searchState,
                            favoriteToggled = state.showOnlyFavorites,
                            onFavoriteToggled = { onAction(OverviewAction.OnFavoriteToggled) },
                            onTextChange = { newTextValue ->
                                searchState = newTextValue
                                onAction(OverviewAction.OnSearchTextChanged(newTextValue.text))
                            }
                        )
                        if (error == null) {
                            ExpandableAreaList(
                                responseData = state.data,
                                showOnlyFavorites = state.showOnlyFavorites,
                                searchInitiated = searchState.text.isNotEmpty() && searchState.text.length > 1,
                                onDetailClick = { resortId ->
                                    onAction(OverviewAction.OnDetailClick(resortId))
                                },
                                onSetFavorite = { resortId ->
                                    onAction(
                                        OverviewAction.OnFavoriteSet(
                                            resortId
                                        )
                                    )
                                },
                            )
                        } else {
                            ErrorState(
                                error = error,
                                canRetry = false,
                                retry = { }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun ErrorState(
    error: DataError,
    canRetry: Boolean,
    retry: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp)
            .padding(top = 60.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Icon(
            modifier = Modifier.padding(8.dp),
            painter = painterResource(id = R.drawable.mountain),
            contentDescription = null
        )
        Text(
            text = error.asUiText().asString(),
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.SemiBold,
            fontSize = 20.sp,
        )
        if (canRetry) {
            Button(
                modifier = Modifier
                    .padding(20.dp),
                onClick = retry
            ) {
                Text(text = "Retry")
            }
        }
    }
}

@Preview
@Composable
private fun OverviewScreenPreview() {
    SnowTheme {
        OverviewScreen(
            state = OverviewState(),
            error = null,
            loading = false,
            onAction = {}
        )
    }
}