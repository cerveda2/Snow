package cz.dcervenka.snow.ui

import android.content.SharedPreferences
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cz.dcervenka.snow.model.Resort
import cz.dcervenka.snow.model.ResponseData
import cz.dcervenka.snow.network.SnowService
import cz.dcervenka.snow.network.safeCall
import cz.dcervenka.snow.ui.overview.OverviewState
import cz.dcervenka.snow.util.DataError
import cz.dcervenka.snow.util.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
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
) : ViewModel() {

    private lateinit var originalData: ResponseData
    private var serverCalled = false

    private val _detailResort = MutableStateFlow<Resort?>(null)
    val detailResort: StateFlow<Resort?> = _detailResort.asStateFlow()

    var state by mutableStateOf(OverviewState())
        private set

    private val _errorEvent = Channel<DataError?>()
    val errorEvent = _errorEvent.receiveAsFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    init {
        loadPlaces()
        viewModelScope.launch {
            snapshotFlow { state }
                .collect { state ->
                    if (!serverCalled) {
                        return@collect
                    }
                    if (state.data.resorts.isNullOrEmpty()) {
                        _errorEvent.send(DataError.Local.SEARCH_EMPTY)
                    } else if (state.showOnlyFavorites && state.data.resorts.none { it.favorite }) {
                        _errorEvent.send(DataError.Local.NO_FAVORITES)
                    } else {
                        _errorEvent.send(null)
                    }
                }
        }
    }

    internal fun loadPlaces() {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                val response = safeCall { snowService.loadPlaces() }
                serverCalled = true
                when (response) {
                    is Result.Error -> _errorEvent.send(response.error)
                    is Result.Success -> {
                        state = state.copy(data = response.data)
                        initializeState()
                        originalData = state.data
                    }
                }
                _isLoading.value = false
            } catch (e: Exception) {
                _isLoading.value = false
                _errorEvent.send(DataError.Network.UNKNOWN)
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

    fun setFavorite(resortId: String, fromDetail: Boolean) {
        val updatedOriginalResorts = originalData.resorts?.map { resort ->
            if (resort.resortId == resortId) {
                resort.copy(favorite = !resort.favorite)
            } else {
                resort
            }
        }
        val updatedResorts = state.data.resorts?.map { resort ->
            if (resort.resortId == resortId) {
                resort.copy(favorite = !resort.favorite)
            } else {
                resort
            }
        }

        if (fromDetail) {
            _detailResort.value = updatedResorts?.find { it.resortId == resortId }
        }

        val updatedData = state.data.copy(resorts = updatedResorts)
        state = state.copy(data = updatedData)

        originalData = originalData.copy(resorts = updatedOriginalResorts)

        val favoriteResorts = updatedOriginalResorts?.filter { it.favorite }?.map { it.resortId }?.toSet() ?: emptySet()
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

    internal fun setDetailResort(resortId: String) {
        _detailResort.value = state.data.resorts?.find { it.resortId == resortId }
    }
}