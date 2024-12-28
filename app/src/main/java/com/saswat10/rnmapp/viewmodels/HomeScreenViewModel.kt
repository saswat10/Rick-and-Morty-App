package com.saswat10.rnmapp.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.saswat10.network.models.domain.Character
import com.saswat10.network.models.domain.CharacterPage
import com.saswat10.rnmapp.repositories.CharacterRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed interface HomeScreenViewState {
    object Loading : HomeScreenViewState
    data class GridDisplay(
        val characters: List<Character> = emptyList(),
    ) : HomeScreenViewState
}

@HiltViewModel
class HomeScreenViewModel @Inject constructor(
    private val repository: CharacterRepository
) : ViewModel() {

    private val _viewState = MutableStateFlow<HomeScreenViewState>(HomeScreenViewState.Loading)
    val viewState: StateFlow<HomeScreenViewState> = _viewState.asStateFlow()

    private val fetchedCharacterPages = mutableListOf<CharacterPage>()

    fun fetchInitialPage() = viewModelScope.launch {
        if (fetchedCharacterPages.isNotEmpty()) return@launch
        val initialPage = repository.fetchCharacterPage(1)
        initialPage
            .onSuccess { characterPage ->
                fetchedCharacterPages.clear()
                fetchedCharacterPages.add(characterPage)

                _viewState.update {
                    return@update HomeScreenViewState.GridDisplay(characters = characterPage.characters)
                }
            }.onFailure {
                // todo
            }
    }

    fun fetchNextPage() = viewModelScope.launch {
        val nextPageIndex = fetchedCharacterPages.size + 1
        repository.fetchCharacterPage(pageNumber = nextPageIndex).onSuccess { characterPage ->
            fetchedCharacterPages.add(characterPage)
            _viewState.update { currentState ->
                val currentCharacters =
                    (currentState as? HomeScreenViewState.GridDisplay)?.characters ?: emptyList()
                return@update HomeScreenViewState.GridDisplay(characters = currentCharacters + characterPage.characters)
            }
        }.onFailure {
            // todo
        }
    }
}

