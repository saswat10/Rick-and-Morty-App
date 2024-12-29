package com.saswat10.rnmapp.viewmodels

import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.runtime.snapshotFlow
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.saswat10.network.models.domain.Character
import com.saswat10.network.models.domain.CharacterStatus
import com.saswat10.rnmapp.repositories.CharacterRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject


sealed interface SearchState {
    object Empty : SearchState
    data class userQuery(val query: String) : SearchState
}

sealed interface ScreenState {
    object Empty : ScreenState
    object Loading : ScreenState
    data class Error(val message: String) : ScreenState
    data class Content(
        val userQuery: String,
        val results: List<Character>,
        val filterState: FilterState
    ) : ScreenState {
        data class FilterState(
            val statuses: List<CharacterStatus>,
            val selectedStatuses: List<CharacterStatus>
        )
    }
}

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val characterRepository: CharacterRepository
) : ViewModel() {
    val basicTextState = TextFieldState()

    @OptIn(FlowPreview::class, ExperimentalCoroutinesApi::class)
    private val searchTextState: StateFlow<SearchState> = snapshotFlow { basicTextState.text }
        .debounce(500)
        .mapLatest { if (it.isBlank()) SearchState.Empty else SearchState.userQuery(it.toString()) }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(stopTimeoutMillis = 2000),
            initialValue = SearchState.Empty
        )

    private val _uiState = MutableStateFlow<ScreenState>(ScreenState.Empty)
    val uiState = _uiState.asStateFlow()

    fun observeUserSearch() = viewModelScope.launch {
        searchTextState.collectLatest { searchState ->
            when (searchState) {
                is SearchState.Empty -> _uiState.update { ScreenState.Empty }

                is SearchState.userQuery -> searchAllCharacters(searchState.query)
            }
        }
    }

    fun toggleStatus(status: CharacterStatus) {
        _uiState.update {
            val currentState = (it as? ScreenState.Content)?: return@update it
            val currentSelectedStatuses =  currentState.filterState.selectedStatuses

            val newStatuses = if(currentSelectedStatuses.contains(status)){
                currentSelectedStatuses - status
            }else{
                currentSelectedStatuses + status
            }

            return@update currentState.copy(
                filterState = currentState.filterState.copy(selectedStatuses = newStatuses)
            )
        }
    }

    private fun searchAllCharacters(query: String) = viewModelScope.launch {
        _uiState.update { ScreenState.Loading }
        characterRepository.fetchAllCharacterByName(query)
            .onSuccess { characters ->
                val allStatuses =
                    characters.map { it.status }.toSet().toList().sortedBy { it.displayName }
                _uiState.update {
                    ScreenState.Content(
                        userQuery = query,
                        results = characters,
                        filterState = ScreenState.Content.FilterState(
                            statuses = allStatuses,
                            selectedStatuses = allStatuses
                        )
                    )
                }
            }
            .onFailure {
                _uiState.update { ScreenState.Error("No search results found") }
            }
    }
}