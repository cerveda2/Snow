package cz.dcervenka.snow.ui.overview

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cz.dcervenka.snow.network.SnowService
import cz.dcervenka.snow.network.safeCall
import cz.dcervenka.snow.ui.util.UiText
import cz.dcervenka.snow.ui.util.asUiText
import cz.dcervenka.snow.util.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class OverviewViewModel @Inject constructor(
    private val snowService: SnowService,
): ViewModel() {

    var state by mutableStateOf(OverviewState())
        private set

    private val _errorEvent = Channel<UiText>()
    val errorEvent = _errorEvent.receiveAsFlow()


    init {
        loadPlaces()
    }

    private fun loadPlaces() {
        viewModelScope.launch {
            try {
                val response = safeCall { snowService.loadPlaces() }
                when (response) {
                    is Result.Error -> _errorEvent.send(response.error.asUiText())
                    is Result.Success -> state = state.copy(data = response.data)
                }
            } catch (e: Exception) {
                Timber.w("Failed to load data or call cancelled")
            }
        }
    }

    fun toggleFavorite() {
        state = state.copy(isFavoriteChecked = !state.isFavoriteChecked)
    }
}