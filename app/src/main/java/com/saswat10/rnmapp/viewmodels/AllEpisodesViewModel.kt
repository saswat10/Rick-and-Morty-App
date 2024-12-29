package com.saswat10.rnmapp.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.saswat10.network.models.domain.Episode
import com.saswat10.rnmapp.repositories.EpisodeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed interface AllEpisodesViewState {
    object Loading : AllEpisodesViewState
    object Error : AllEpisodesViewState
    data class Success(val data: Map<String, List<Episode>>) : AllEpisodesViewState

}

@HiltViewModel
class AllEpisodesViewModel @Inject constructor(
    private val repository: EpisodeRepository
) : ViewModel() {
    private val _viewState = MutableStateFlow<AllEpisodesViewState>(AllEpisodesViewState.Loading)
    var viewState = _viewState.asStateFlow()

    fun refreshAllEpisodes(forceRefresh: Boolean = false) = viewModelScope.launch {
        if (forceRefresh) _viewState.update { AllEpisodesViewState.Loading }
        repository.fetchAllEpisodes().onSuccess { episodeList ->
            _viewState.update {
                AllEpisodesViewState.Success(
                    data = episodeList.groupBy { it.seasonNumber }
                        .mapKeys { "Season ${it.key}" }
                )
            }

        }.onFailure {
            _viewState.update { AllEpisodesViewState.Error }

        }
    }
}