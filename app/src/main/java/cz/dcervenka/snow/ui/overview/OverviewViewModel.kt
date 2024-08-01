package cz.dcervenka.snow.ui.overview

import android.content.SharedPreferences
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cz.dcervenka.snow.model.ResponseData
import cz.dcervenka.snow.network.SnowService
import cz.dcervenka.snow.network.safeCall
import cz.dcervenka.snow.ui.util.UiText
import cz.dcervenka.snow.ui.util.asUiText
import cz.dcervenka.snow.util.DataError
import cz.dcervenka.snow.util.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

const val SHARED_PREFERENCES_NAME = "sharedPref"
const val KEY_FAVORITES = "KEY_FAVORITES"

@HiltViewModel
class OverviewViewModel @Inject constructor(
    private val snowService: SnowService,
    private val sharedPreferences: SharedPreferences,
): ViewModel() {

    private lateinit var originalData: ResponseData

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
                    is Result.Success -> {
                        state = state.copy(data = response.data)
                        originalData = response.data
                        initializeState()
                    }
                }
            } catch (e: Exception) {
                _errorEvent.send(DataError.Network.UNKNOWN.asUiText())
                Timber.w("Failed to load data or call cancelled")
            }
        }
    }

    fun search(query: String) {
        state = if (query.length >= 2) {
            val filteredResorts = originalData.resorts?.filter { resort ->
                resort.name.contains(query, ignoreCase = true)
            } ?: emptyList()
            state.copy(
                data = state.data.copy(resorts = filteredResorts),
                search = TextFieldValue(query)
            )
        } else {
            state.copy(
                data = originalData,
                search = TextFieldValue(query)
            )
        }
    }

    fun toggleFavorite() {
        state = state.copy(showOnlyFavorites = !state.showOnlyFavorites)
    }

    fun setFavorite(resortId: String) {
        val updatedResorts = state.data.resorts?.map { resort ->
            if (resort.resortId == resortId) {
                resort.copy(favorite = !resort.favorite)
            } else {
                resort
            }
        }
        val updatedData = state.data.copy(resorts = updatedResorts)
        state = state.copy(data = updatedData)

        val favoriteResorts = updatedResorts?.filter { it.favorite }?.map { it.resortId }?.toSet() ?: emptySet()
        saveFavoritesToPreferences(favoriteResorts)
    }

    private fun initializeState() {
        val favoriteResorts = getFavoritesFromPreferences()

        val resorts = state.data.resorts?.map { resort ->
            resort.copy(favorite = favoriteResorts.contains(resort.resortId))
        }

        state = state.copy(data = state.data.copy(resorts = resorts))
    }

    private fun getFavoritesFromPreferences(): Set<String> {
        return sharedPreferences.getStringSet(KEY_FAVORITES, emptySet()) ?: emptySet()
    }

    private fun saveFavoritesToPreferences(favoriteResorts: Set<String>) {
        with(sharedPreferences.edit()) {
            putStringSet(KEY_FAVORITES, favoriteResorts)
            apply()
        }
    }
}